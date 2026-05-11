import pandas as pd
import requests
import time

# Read CSV
df = pd.read_csv('algeria_pipeline_coordinates.csv')
print(f"Processing {len(df)} coordinates...\n")

# Wilaya codes (01-58) - Official Algeria administrative codes
WILAYA_CODES = {
    'Adrar': '01', 'Chlef': '02', 'Laghouat': '03', 'Oum El Bouaghi': '04',
    'Batna': '05', 'Béjaïa': '06', 'Biskra': '07', 'Béchar': '08',
    'Blida': '09', 'Bouira': '10', 'Tamanrasset': '11', 'Tébessa': '12',
    'Tlemcen': '13', 'Tiaret': '14', 'Tizi Ouzou': '15', 'Algiers': '16',
    'Djelfa': '17', 'Jijel': '18', 'Sétif': '19', 'Saïda': '20',
    'Skikda': '21', 'Sidi Bel Abbès': '22', 'Annaba': '23', 'Guelma': '24',
    'Constantine': '25', 'Médéa': '26', 'Mostaganem': '27', "M'Sila": '28',
    'Mascara': '29', 'Ouargla': '30', 'Oran': '31', 'El Bayadh': '32',
    'Illizi': '33', 'Bordj Bou Arréridj': '34', 'Boumerdès': '35',
    'El Tarf': '36', 'Tindouf': '37', 'Tissemsilt': '38', 'El Oued': '39',
    'Khenchela': '40', 'Souk Ahras': '41', 'Tipaza': '42', 'Mila': '43',
    'Aïn Defla': '44', 'Naâma': '45', 'Aïn Témouchent': '46',
    'Ghardaïa': '47', 'Relizane': '48', 'Timimoun': '49',
    'Bordj Badji Mokhtar': '50', 'Ouled Djellal': '51', 'Béni Abbès': '52',
    'In Salah': '53', 'In Guezzam': '54', 'Touggourt': '55',
    'Djanet': '56', "El M'Ghair": '57', 'El Meniaa': '58'
}

def get_wilaya_code(wilaya_name):
    """Get wilaya numeric code from name"""
    # Try exact match first
    if wilaya_name in WILAYA_CODES:
        return WILAYA_CODES[wilaya_name]
    
    # Try partial match (case insensitive)
    wilaya_lower = wilaya_name.lower()
    for name, code in WILAYA_CODES.items():
        if name.lower() in wilaya_lower or wilaya_lower in name.lower():
            return code
    
    return '00'  # Unknown

def reverse_geocode(lat, lon):
    """Get location name, commune, wilaya with codes from coordinates"""
    try:
        response = requests.get(
            'https://nominatim.openstreetmap.org/reverse',
            params={
                'lat': lat,
                'lon': lon,
                'format': 'json',
                'addressdetails': 1,
                'accept-language': 'en'
            },
            headers={'User-Agent': 'AlgeriaPipelineMapper/1.0'},
            timeout=15
        )
        
        if response.status_code == 200:
            data = response.json()
            addr = data.get('address', {})
            
            # Extract wilaya (province/state level)
            wilaya_name = (addr.get('state') or 
                          addr.get('province') or 
                          addr.get('county') or 
                          'Unknown')
            
            # Get wilaya code
            wilaya_code = get_wilaya_code(wilaya_name)
            
            # Extract commune (municipality level)
            commune_name = (addr.get('municipality') or 
                           addr.get('town') or 
                           addr.get('city') or 
                           addr.get('village') or
                           addr.get('hamlet') or
                           'Unknown')
            
            # Extract postal code (commune code embedded in it)
            postcode = addr.get('postcode', '')
            commune_code = postcode[:5] if postcode else wilaya_code + '000'
            
            # Extract location name (more specific)
            location = (addr.get('suburb') or 
                       addr.get('neighbourhood') or 
                       addr.get('locality') or 
                       addr.get('quarter') or
                       commune_name)
            
            return location, commune_name, commune_code, wilaya_name, wilaya_code
            
    except Exception as e:
        print(f"  Error at {lat},{lon}: {str(e)[:50]}")
    
    return 'Unknown', 'Unknown', '00000', 'Unknown', '00'

# Process coordinates
locations = []
commune_names = []
commune_codes = []
wilaya_names = []
wilaya_codes = []

start_time = time.time()

for idx, row in df.iterrows():
    loc, com_name, com_code, wil_name, wil_code = reverse_geocode(
        row['Latitude'], row['Longitude']
    )
    
    locations.append(loc)
    commune_names.append(com_name)
    commune_codes.append(com_code)
    wilaya_names.append(wil_name)
    wilaya_codes.append(wil_code)
    
    if (idx + 1) % 50 == 0:
        elapsed = (time.time() - start_time) / 60
        remaining = ((len(df) - (idx + 1)) * 1.1) / 60
        print(f"Progress: {idx + 1}/{len(df)} ({(idx+1)/len(df)*100:.1f}%) | "
              f"Elapsed: {elapsed:.1f}min | Est. remaining: {remaining:.1f}min")
    
    # Rate limit: 1 request per second
    time.sleep(1.1)

# Save results with codes
df['Location'] = locations
df['Commune_Name'] = commune_names
df['Commune_Code'] = commune_codes
df['Wilaya_Name'] = wilaya_names
df['Wilaya_Code'] = wilaya_codes

output_file = 'algeria_pipeline_coordinates_with_locations.csv'
df.to_csv(output_file, index=False, encoding='utf-8-sig')

total_time = (time.time() - start_time) / 60

print(f"\n{'='*70}")
print("COMPLETE!")
print(f"{'='*70}")
print(f"Time: {total_time:.2f} minutes")
print(f"Output: {output_file}\n")

# Statistics
print("Wilaya Distribution:")
wilaya_stats = df.groupby(['Wilaya_Code', 'Wilaya_Name']).size().reset_index(name='Count')
wilaya_stats = wilaya_stats.sort_values('Wilaya_Code')
for _, row in wilaya_stats.head(20).iterrows():
    print(f"  {row['Wilaya_Code']} - {row['Wilaya_Name']:25s}: {row['Count']:3d} coordinates")

print("\nCommune Distribution (top 20):")
commune_stats = df.groupby(['Commune_Code', 'Commune_Name']).size().reset_index(name='Count')
commune_stats = commune_stats.sort_values('Count', ascending=False)
for _, row in commune_stats.head(20).iterrows():
    print(f"  {row['Commune_Code']} - {row['Commune_Name']:25s}: {row['Count']:3d}")

print(f"\nSample data:")
print(df[['FID', 'Longitude', 'Latitude', 'Location', 
          'Commune_Code', 'Commune_Name', 'Wilaya_Code', 'Wilaya_Name']].head(15))


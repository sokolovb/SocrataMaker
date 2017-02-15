[dataset]
```property
URL = https://data.montgomerycountymd.gov/api/views/icn6-v9z3/rows.json?accessType=DOWNLOAD
Catalog URL = https://catalog.data.gov/dataset/crime
Id = icn6-v9z3
Name = Crime
Description = Updated daily postings on Montgomery County?s open data website, dataMontgomery, provide the public with direct access to crime statistic databases - including raw data, mapping and search function...
Category = Public Safety
Tags = [crime, incidents, robberies, theft, assault, district, arson, montgomery, maryland, mdcoordinationcrime]
Created = 2015-03-24T23:42:50Z
Publication Date = 2016-11-15T23:48:05Z
Rows Updated = 2017-02-09T15:01:26Z
```

[columns]
```ls
| Name                   | Field Name             | Data Type     | Render Type   | Schema Type    | Included | 
| ====================== | ====================== | ============= | ============= | ============== | ======== | 
| Incident ID            | incident_id            | text          | text          | series tag     | Yes      | 
| CR Number              | case_number            | number        | text          | numeric metric | Yes      | 
| Dispatch Date / Time   | date                   | calendar_date | calendar_date | time           | Yes      | 
| Class                  | incident_type          | text          | text          | series tag     | Yes      | 
| Class Description      | narrative              | text          | text          | series tag     | Yes      | 
| Police District Name   | district               | text          | text          | series tag     | Yes      | 
| Block Address          | location               | text          | text          | series tag     | Yes      | 
| City                   | city                   | text          | text          | series tag     | Yes      | 
| State                  | state                  | text          | text          | series tag     | Yes      | 
| Zip Code               | zip_code               | number        | number        | numeric metric | Yes      | 
| Agency                 | agency                 | text          | text          | series tag     | Yes      | 
| Place                  | place                  | text          | text          | series tag     | Yes      | 
| Sector                 | sector                 | number        | text          | numeric metric | Yes      | 
| Beat                   | beat                   | text          | text          | series tag     | Yes      | 
| PRA                    | pra                    | number        | text          | numeric metric | Yes      | 
| Start Date / Time      | start_date             | calendar_date | calendar_date |                | No       | 
| End Date / Time        | end_date               | calendar_date | calendar_date |                | No       | 
| Police District Number | police_district_number | text          | text          | series tag     | Yes      | 
| Address Number         | address_number         | number        | text          | numeric metric | Yes      | 
```

[time]
```ls
Value = date
Format & Zone = yyyy-MM-dd'T'HH:mm:ss
```

[series]
```ls
Metric Prefix = 
Included Fields = *
Excluded Fields = start_date,end_date
Annotation Fields = 
```

[commands]
```ls
series e:icn6-v9z3 d:2014-08-25T20:04:00.000Z t:police_district_number=3D t:incident_type=2791 t:sector=G t:beat=3G1 t:location="8100  GEORGIA AVE" t:narrative="ALL OTHER NON-TRAFFIC CRIM OFFENSES" t:state=MD t:agency=MCPD t:district="SILVER SPRING" t:place="Street - Commercial" t:incident_id=200986023 t:city="SILVER SPRING" m:address_number=8100 m:case_number=14041165 m:zip_code=20910 m:pra=94 

series e:icn6-v9z3 d:2014-09-01T13:09:00.000Z t:police_district_number=2D t:incident_type=617 t:sector=D t:beat=2D3 t:location="10100  CEDAR LN" t:narrative="LARCENY FROM BUILDING OVER $200" t:state=MD t:agency=MCPD t:district=BETHESDA t:place="Residence - Shed" t:incident_id=200986810 t:city=KENSINGTON m:address_number=10100 m:case_number=14042282 m:zip_code=20895 m:pra=180 

series e:icn6-v9z3 d:2014-08-29T22:29:00.000Z t:police_district_number=4D t:incident_type=433 t:sector=K t:beat=4K2 t:location="12500  GOODHILL RD" t:narrative="AGG ASSLT OTHER WPN SPOUSE/PARTNER" t:state=MD t:agency=MCPD t:district=WHEATON t:place="Street - Residential" t:incident_id=200986526 t:city="SILVER SPRING" m:address_number=12500 m:case_number=14041909 m:zip_code=20906 m:pra=334 
```

[meta-commands]
```ls
metric m:case_number p:integer l:"CR Number" d:"Police Report Number" t:dataTypeName=number 

metric m:zip_code l:"Zip Code" d:"Zip Code" t:dataTypeName=number 

metric m:pra p:integer l:PRA d:"Police Response Area, a subset of Beat" t:dataTypeName=number 

metric m:address_number p:integer l:"Address Number" d:"House or Business Number" t:dataTypeName=number 

entity e:icn6-v9z3 l:Crime t:url=https : //data.montgomerycountymd.gov/api/views/icn6-v9z3 

property e:icn6-v9z3 t:meta.view d:2017-02-15T09:43:49.899Z v:id=icn6-v9z3 v:category="Public Safety" v:averageRating=0 v:name=Crime 

property e:icn6-v9z3 t:meta.view.license d:2017-02-15T09:43:49.899Z v:name="Public Domain" 

property e:icn6-v9z3 t:meta.view.owner d:2017-02-15T09:43:49.899Z v:id=ajn4-zy65 v:screenName="MCG ESB Service" v:roleName=administrator v:displayName="MCG ESB Service" 

property e:icn6-v9z3 t:meta.view.tableauthor d:2017-02-15T09:43:49.899Z v:id=ajn4-zy65 v:screenName="MCG ESB Service" v:roleName=administrator v:displayName="MCG ESB Service" 
```

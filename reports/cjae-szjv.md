[dataset]
```property
URL = https://data.cdc.gov/api/views/cjae-szjv/rows.json?accessType=DOWNLOAD
Catalog URL = https://catalog.data.gov/dataset/air-quality-measures-on-the-national-environmental-health-tracking-network
Id = cjae-szjv
Name = Air Quality Measures on the National Environmental Health Tracking Network
Description = The Environmental Protection Agency (EPA) provides air pollution data about ozone and particulate matter (PM2.5) to CDC for the Tracking Network. The EPA maintains a database called the Air Quality...
Tags = [environmental hazard, air quality, air quality system, daily maximum 8 hour average concentration, daily 24 hour average concentration, hourly observations, site monitoring data, regulatory resour...
Created = 2015-08-12T12:10:33Z
Publication Date = 2015-08-13T14:09:10Z
Rows Updated = 2015-08-12T17:58:37Z
```

[columns]
```ls
| Name                | Field Name          | Data Type | Render Type | Schema Type    | Included | 
| =================== | =================== | ========= | =========== | ============== | ======== | 
| MeasureId           | measureid           | text      | number      | series tag     | Yes      | 
| MeasureName         | measurename         | text      | text        | series tag     | Yes      | 
| MeasureType         | measuretype         | text      | text        | series tag     | Yes      | 
| StratificationLevel | stratificationlevel | text      | text        | series tag     | Yes      | 
| StateFips           | statefips           | number    | text        | numeric metric | Yes      | 
| StateName           | statename           | text      | text        | series tag     | Yes      | 
| CountyFips          | countyfips          | number    | text        | numeric metric | Yes      | 
| CountyName          | countyname          | text      | text        | series tag     | Yes      | 
| ReportYear          | reportyear          | number    | text        | time           | Yes      | 
| Value               | value               | number    | number      | numeric metric | Yes      | 
| Unit                | unit                | text      | text        | series tag     | Yes      | 
| UnitName            | unitname            | text      | text        | series tag     | Yes      | 
| DataOrigin          | dataorigin          | text      | text        | series tag     | Yes      | 
| MonitorOnly         | monitoronly         | number    | number      | numeric metric | Yes      | 
```

[time]
```ls
Value = reportyear
Format & Zone = yyyy
```

[series]
```ls
Metric Prefix = 
Included Fields = *
Excluded Fields = 
Annotation Fields = 
```

[commands]
```ls
series e:cjae-szjv d:1999-01-01T00:00:00.000Z t:unit="No Units" t:measurename="Number of days with maximum 8-hour average ozone concentration over the National Ambient Air Quality Standard" t:countyname=Elmore t:dataorigin="Monitor Only" t:unitname="No Units" t:measureid=83 t:measuretype=Counts t:statename=Alabama t:stratificationlevel="State x County" m:countyfips=1051 m:statefips=1 m:value=5 m:monitoronly=1 

series e:cjae-szjv d:1999-01-01T00:00:00.000Z t:unit="No Units" t:measurename="Number of days with maximum 8-hour average ozone concentration over the National Ambient Air Quality Standard" t:countyname=Jefferson t:dataorigin="Monitor Only" t:unitname="No Units" t:measureid=83 t:measuretype=Counts t:statename=Alabama t:stratificationlevel="State x County" m:countyfips=1073 m:statefips=1 m:value=39 m:monitoronly=1 

series e:cjae-szjv d:1999-01-01T00:00:00.000Z t:unit="No Units" t:measurename="Number of days with maximum 8-hour average ozone concentration over the National Ambient Air Quality Standard" t:countyname=Lawrence t:dataorigin="Monitor Only" t:unitname="No Units" t:measureid=83 t:measuretype=Counts t:statename=Alabama t:stratificationlevel="State x County" m:countyfips=1079 m:statefips=1 m:value=28 m:monitoronly=1 
```

[meta-commands]
```ls
metric m:statefips p:integer l:StateFips d:"Unique two-digit numeric code assigned to each state in the United States." t:dataTypeName=number 

metric m:countyfips p:integer l:CountyFips d:"Unique five-digit code assigned to each county in the United States. The first two digits are the state FIPS code and the last three digits correspond to the county within that state." t:dataTypeName=number 

metric m:value p:integer l:Value d:"Data value specific to data measure in the stratified geography" t:dataTypeName=number 

metric m:monitoronly p:integer l:MonitorOnly d:"Flags if data is Monitor only, compared to monitor and modeled data." t:dataTypeName=number 

entity e:cjae-szjv l:"Air Quality Measures on the National Environmental Health Tracking Network" t:url=https : //data.cdc.gov/api/views/cjae-szjv 

property e:cjae-szjv t:meta.view d:2017-02-15T10:09:52.707Z v:id=cjae-szjv v:averageRating=0 v:name="Air Quality Measures on the National Environmental Health Tracking Network" 

property e:cjae-szjv t:meta.view.owner d:2017-02-15T10:09:52.707Z v:id=94g5-7as2 v:screenName=Tracking v:roleName=publisher v:displayName=Tracking 

property e:cjae-szjv t:meta.view.tableauthor d:2017-02-15T10:09:52.707Z v:id=94g5-7as2 v:screenName=Tracking v:roleName=publisher v:displayName=Tracking 

property e:cjae-szjv t:meta.view.metadata.custom_fields.common_core d:2017-02-15T10:09:52.707Z v:Contact_Email=cak8@cdc.gov v:Publisher="Environmental Health Tracking Network" v:Homepage=http : //ephtracking.cdc.gov/ v:Is_Quality_Data=yes v:Contact_Name="Craig Kassinger" v:Bureau_Code=009 : 20 v:Language=English v:Update_Frequency=irregular v:Program_Code=009 : 032 
```

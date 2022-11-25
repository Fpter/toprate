import pandas as pd
import sqlite3
from datetime import datetime


conn = sqlite3.connect('weather.sqlite')
conn.execute("DROP TABLE WEATHER")
conn.execute('''CREATE TABLE WEATHER
         (
         NAME           CHAR(50)    NOT NULL,
         TEMP            REAL     NOT NULL,
         DATE           CHAR(50),
         TIME          CHAR(50),
         PRIMARY KEY (DATE, TIME, NAME))
         ''')
print("Opened database successfully")

# read data from file json and insert to database
df = pd.read_json('data.json')
for index, row in df.iterrows():
  name = row.get('city_name')

  date_str = row.get('dt_iso')
  date_split = date_str.split(' ')
  date = date_split[0]
  time = date_split[1]

  temp = row.get('main')['temp']
  conn.execute("insert into WEATHER (NAME, TEMP, DATE, TIME) values (?, ?, ?, ?)", (name, temp, date, time))
  conn.commit()

# read all data
cur = conn.cursor()
cur.execute("SELECT * FROM WEATHER")

rows = cur.fetchall()

for row in rows:
  print(row)
conn.close()
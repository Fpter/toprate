from flask import Flask, render_template, request
import sqlite3
import pandas as pd
from datetime import datetime

app = Flask(__name__)

@app.route('/')
def index():
    # get request param
    date = request.args.get('date')
    
    # open connection to database
    conn = sqlite3.connect('weather.sqlite')
    cur = conn.cursor()

    # get min temp
    cur.execute("select time,temp from WEATHER where date = ? order by temp limit 1", [date])
    rows_min = cur.fetchall()
    min = 'Khong co du lieu'
    if len(rows_min) > 0:
        min = f'The coldest hour: {rows_min[0][0]} with temp {rows_min[0][1]} '

    # get max temp
    max = 'Khong co du lieu'
    cur.execute("select time,temp from WEATHER where date = ? order by temp desc limit 1", [date])
    rows_max = cur.fetchall()
    if len(rows_max) > 0:
        max = f'The warmest hour: {rows_max[0][0]} with temp {rows_max[0][1]} '

    # close connection
    cur.close()
    conn.close()
    return render_template('weather.html', min = min, max = max, data = date)

if __name__ == '__main__':
   app.run(debug = True)
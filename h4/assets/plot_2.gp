set datafile separator ","
set terminal pngcairo size 800,600 enhanced font 'Verdana,10'
set output '6.png'
set boxwidth 0.5
set style fill solid
set title "Average Temperature by Continent"
set xlabel "Continent"
set ylabel "Average Temperature (°„C)"
set grid
plot 'avg_temp_by_continent.csv' using 2:xtic(1) with boxes lc rgb "blue" title 'Avg Temp'

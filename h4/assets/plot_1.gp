set datafile separator ","
set terminal pngcairo size 800,600 enhanced font 'Verdana,10'
set output 'temp_variation_by_continent.png'
set boxwidth 0.5
set style fill solid
set title "Temperature Variation by Continent"
set xlabel "Continent"
set ylabel "Temperature Variation (°„C)"
set grid
plot 'temp_variation_by_continent.csv' using 2:xtic(1) with boxes lc rgb "blue" title 'Temp Variation'
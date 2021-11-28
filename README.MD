# Requirements

To run the app you reqiure:
* `docker 20.10.8`

# How to run

Run following command:

`docker run -v "<data>:/input" -v "<output>:/output" pkaron/aoin:latest <params>
`
Where:
* `<data>` – absolute path to directory containing one or more input datasets,
* `<output>` – directory into which results files will be saved
* `<params>` – program parameters. See section below.

Example: running from inside this repository: `docker run -v "$(pwd)/data:/input" -v "$(pwd)/output:/output" pkaron/aoin:latest -a ref`

# Program parameters

```
Options: 
    --algorithm, -a -> Algorithm: ref - reference, gen - genetic (always required) { Value should be one of [ref, gen] }
    --output, -o -> Output file name { String }
    --help, -h -> Usage info 
```

# Dataset format

The program accepts a csv file containing description of single problem instance:
```text
30,,            # Weight limit followed by two commas
Lampa,2,40      # List of items: name,weight,value
Ksiazka,2,160
Blender,3,70
Kubek,1,1
Krzeslo,1,1
```

# Output format

Program returns comma separated .csv file with all result data for all input dataset files:
```text
Input filename,Algorithm,Time in millis,Weight limit,Calculated weight,Calculated value
```

E.g.:
```text
dataset1.csv,REF,63234,30,29,120
dataset2.csv,REF,12234,10,7,10
```

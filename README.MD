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

Example: running from inside this repository: `docker run -v "$(pwd)/data:/input" -v "$(pwd)/output:/output" pkaron/aoin:latest reference`

# Program parameters

```
Usage: aoin options_list
Subcommands: 
    reference - Run reference algorithm - dynamic programming
    genetic - Run genetic algorithm

Options: 
    --help, -h -> Usage info 
```

Run `aoin <subcommand> --help` to see available parameters for each algorithm.

### Genetic algorithm parameters

```text
Options: 
    --input, -i [/input] -> Input directory path. Don't use while using docker version { String }
    --outputDir, -od [/output] -> Output directory path. Don't use while using docker version { String }
    --output, -o -> Output file name { String }
    --numOfGenerations [100] { Int }
    --genesisPopulationSize [20] { Int }
    --tournamentSize [5] { Int }
    --numberOfParentsForCrossover [2] { Int }
    --mutationRate [0.05] { Double }
    --help, -h -> Usage info 
```

# Dataset format

The program accepts a csv file containing description of single problem instance:

CSV (file must have `.csv` extension):
```text
30,,            # Weight limit followed by two commas
Lampa,2,40      # List of items: name,weight,value
Ksiazka,2,160
Blender,3,70
Kubek,1,1
Krzeslo,1,1
```

or generic (file without extension):
```text
5 80     # Header: Number of items <space> Weight limit
33 15    # Item: Value <space> Weight
24 20
36 17
37 8
12 31
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

# How to run in IntelliJ (skipping Docker)

Use run configuration from `.run/MainKt.run.xml`.

Otherwise, you need to pass parameters `-i "./data" -od "./output" <algorithm>` in run configuration.

# Genetic Algorithm description

Mainframe of the algorithm:
1. Create input genes pool from items
2. Generate genesis population
3. Evolve population
4. Repeat 3. specified number of times

## Genesis population generator

Genesis population is based on provided required population size. A loop is run that number
times. Each time genes list is randomized and each element is attempted to be put into backpack
(chromosome). Each chromosome is added to the population.

## Evolution

Evolution is based on these steps:
1. Initialize new

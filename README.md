# Laboratorio di Linguaggi Formali e Traduttori

![GitHub last commit](https://img.shields.io/github/last-commit/dimuzzo/lft-project-23-24?style=flat-square&logo=github&label=Last%20Commit)
![GitHub repo size](https://img.shields.io/github/repo-size/dimuzzo/lft-project-23-24?style=flat-square&logo=github&label=Repo%20Size)
![GitHub stars](https://img.shields.io/github/stars/dimuzzo/lft-project-23-24?style=flat-square&logo=github&label=Stars)

### Progetto 
Il progetto di laboratorio consiste in una serie di esercitazioni assistite mirate allo sviluppo di un semplice traduttore.  

Il corretto svolgimento di tali esercitazioni presuppone una buona conoscenza del linguaggio di programmazione Java e degli argomenti di teoria del corso Linguaggi Formali e Traduttori. 

---

### Esercizio 1: Implementazione di un DFA in Java

Lo scopo di questo esercizio e l’implementazione di un metodo Java che sia in grado di discrimi nare le stringhe del linguaggio riconosciuto da un automa a stati finiti deterministico (DFA) dato.

Il primo automa che prendiamo in considerazione è definito sull’alfabeto {0, 1} e riconosce le stringhe in cui compaiono almeno 3 zeri consecutivi.

L’automa è implementato nel metodo scan che accetta una stringa s e restituisce un valore booleano che indica se la
2 stringa appartiene o meno al linguaggio riconosciuto dall’automa. 

Lo stato dell’automa e rappresentato per mezzo di una variabile intera state, mentre la variabile i contiene l’indice del
prossimo carattere della stringa s da analizzare. 

Il corpo principale del metodo e un ciclo che,  analizzando il contenuto della stringa s un carattere alla volta, effettua un cambiamento dello
stato dell’automa secondo la sua funzione di transizione. 

Notare che l’implementazione assegna il valore −1 alla variabile state se viene incontrato un simbolo diverso da 0 e 1. Tale valore non
e uno stato valido, ma rappresenta una condizione di errore irrecuperabile. 

---

### Esercizio 2: Analisi lessicale

Gli esercizi di questa sezione riguardano l’implementazione di un analizzatore lessicale per un semplice linguaggio di programmazione. Lo scopo di un analizzatore lessicale e di leggere un testo e di ottenere una corrispondente sequenza di token, dove un token corrisponde ad un’unità lessicale, come un numero, un identificatore, un operatore relazionale, una parola chiave, ecc.

Nelle sezioni successive, l’analizzatore lessicale da implementare sara poi utilizzato per fornire l’input a programmi di analisi sintattica e di traduzione.

Gli identificatori corrispondono all’espressione regolare: `{(a+...+z+A+...+Z)(a+...+z+A+...+Z+0+...+9)^*}`

e i numeri corrispondono all’espressione regolare:  `0 + (1 + ... + 9)(0 + ... + 9)`

L’analizzatore lessicale dovra ignorare tutti i caratteri riconosciuti come “spazi” (incluse le tabulazioni e i ritorni a capo), ma dovrà segnalare la presenza di caratteri illeciti, quali ad esempio # o @.

---

### Esercizio 3:

Leggere file .PDF

---

### Esercizio 4:

Leggere file .PDF

---

### Esercizio 5:

L’obiettivo di quest’ultima parte di laboratorio e di quello di realizzare un traduttore per i programmi scritti nel linguaggio di programmazione semplice, che chiameremo P, visto nell’esercizio 3.2. Il traduttore deve generare bytecode eseguibile dalla Java Virtual Machine (JVM).

Generare bytecode eseguibile direttamente dalla JVM non e un’operazione semplice a causa della complessita del formato dei file .class (che tra l’altro e un formato binario). 

Il bytecode verrà quindi generato avvalendoci di un linguaggio mnemonico che fa riferimento alle istruzioni della JVM (linguaggio assembler) e che successivamente verrà tradotto nel formato .class dal programma assembler. 

Il linguaggio mnemonico utilizzato fa riferimento all’insieme delle istruzioni della JVM [e l’assembler effettua una traduzione 1-1 delle istruzioni mnemoniche nella corrispondente istruzione (opcode) della JVM. 

Il programma assembler che utilizzeremo si chiama Jasmin (distribuzione e manuale sono disponibili all’indirizzo http://jasmin.sourceforge.net/).

---

> Created with passion by [dimuzzo](https://github.com/dimuzzo)

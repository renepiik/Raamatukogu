# Projekt Raamatukogu

Autorid: René Piik ja Sander Roosalu

## Kirjeldus

Programm võimaldab luua ja hallata isiklikku raamatukogude kataloogi. Programmi mõte on selle lihtsuses: raamatute nägemiseks, lisamiseks ja muutmiseks pole tarvis internetiühendust ega kasutada suuri ja keerulisi kasutajaliideseid. Programm on väike (alla 25kB) ja kiire.

## Klassid

### Main
Siin luuakse klassi ConsoleInterface isend, mille kaudu suhtleb kasutaja programmiga. Samuti toimub main() meetodis kasutaja sisendi lugemine ja tõlgendamine.

### ConsoleInterface
_Singleton_ klass, mille kaudu kasutaja programmiga suhtleb. Siin paikneb kogu programmi loogika, mis seondub info käitlemisega. Klassi isend salvestab kasutaja sisestatud info ja programmi sulgemisel kirjutab selle failidesse.

### Startup
Klass, mille meetodit Initialize() jooksutatakse ühe korra uue _ConsoleInterface_ tüüpi objekti loomisel. Tagastab ./libs/ kausta salvestatud raamatukogude info.

### Library
Raamatukogu klass, hoiustab _Book_ tüüpi objekte. Meetod Library.save() salvestab vastava raamatukogu info samanimelisse .csv faili ./libs/ kaustas.

### Book
Raamatu klass, hoiustab raamatu isendiga seotud informatsiooni.

### ISBN
Väike klass selleks, et uute raamatute info sisestamisel saaks kontrollida ISBN koodi õigsust.

## Protsess
Pärast projekti algse versiooni kirjutamist jagasime edasise töö laias laastus kaheks:
1. Andmete lugemine/kirjutamine (Sander)
2. Kasutajaga terminalis suhtlemine (René)

Kui mõlemad tööd said valmis, _merge_-sime harud ja tegime väikseid muudatusi edaspidi _master_ harusse. Suhtlesime teineteisega jooksvalt, kui tekkis uusi ideid ja probleeme koodiga, ning jagasime tööülesandeid kordamööda.

## Panus
### René
Kirjutas programmi algse versiooni jaoks klassid ISBN, _Library_ ja _Book_. Implementeeris kasutajaga suhtlemise klassi _ConsoleInterface_ abil.

### Sander
Kirjutas klassi _Startup_, ühtlustas keeleliselt kasutajaga suhtlemise, implementeeris andmete salvestamise ja lugemise .csv formaadis.

## Mured
Projekti tegemisel tuli ise otsida infot selle kohta, kuidas andmeid salvestada ja hiljem normaalsel kujul sisselugeda. Kasuks tuli ka _enum_ kasutamine ja _singleton_ tüüpi klassi kasutamine, millest polnud loengutes eriti palju juttu.

## Hinnang
Tööga saime hästi hakkama, ühtegi ületamatut muret ei tekkinud. Tulevikus saaks kindlasti kirjutada juurde graafilise liidese. Samuti tuleks mõelda pisut, kuidas teha raamatute (raamatukogude) lisamist ja andmete muutmist kasutajale mugavamaks.

## Testimine
Testimine toimus jooksvalt. Uute meetodite kirjutamisel testisime, kuidas need eri sorti sisendite puhul käituvad ja kas soovitud funktsionaalsus on tagatud. Pärast uue koodi kirjutamist lugesime ja testisime ka teineteise koodi.

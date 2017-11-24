# Übung 2

## Aufgabe 1

### Anti-Patterns:
- Der Blob:
    - Von den Klassen abgesehen ist die Datei selbst ein Blob, da sie aus unzähligen Klassen besteht, die man auf mehrere Dateien auftrennen sollte
    - Von den Klassen ist die Hauptklasse SDRaytracer der Blob, da sie nicht nur die Mainmethode beherbergt, sondern ebenfalls alle wesentlichen Methoden zum Raytracing bereitstellt, das JFrame Fenster aufbaut und auch die Belichtung übernimmt. Dem entsprechend hat sie viele Attribute.
- Poltergeist:
    - Ein Beispiel dafür sind Klassen wie Triangle, IPoint und Light, die außer einem Konstruktor und 2-3 Attributen keine weiteren Funktionen bieten. Sie dienen nur als Daten. Haben aber Referenzen auf andere Klassen die ebnfalls nur als Daten dienen. Diese könnte man zusammenfassen.
- Funktionale Zerlegung:
    - Diese kann man gut an den Klassen DEBUG und Cube erkennen, die nicht einmal einen Konstruktor besitzen sondern lediglich eine einzelne Methode.

### Bad-Smells:
- Duplizierter Code:
    - nicht vorhanden
- Unangebrachte Intimität:
    - In Cube werden Triangles zu einer Liste hinzugefügt, die wiederum aus Vec3D Objekten bestehen. Die Klasse Cube trägt nichts zur Methode bei und sie würde genau so in jeder anderen Klasse funktionieren. Man könnte sie also auch nach Triangle verschieben
- Switch-Anweisungen:
    - Es gibt keine Switch-Anweisung
- Ausgeschlagenes Erbe:
    - Typ-B tritt nicht auf, da die einzige überschrieben Methode keyPressed() ist, welches die korrekten Parameter besitzt
    - Typ-A kann sich höchstens auf SDRaytrace beziehen, welche von JFrame erbt. Es werden jedoch einige Methoden der Oberklasse verwendet, um die Szene aufzubauen. Da ein JFrame ein essenzieller Teil von Java-Spring ist, wäre es auch nicht ratsam von einer höheren Klasse zu erben.
    - 
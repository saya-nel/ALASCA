# ALASCA

## mono-jvm

```
java -ea -cp "jars/*" main.java.deployment.CVM
```

## multi-jvm

Le lancement multi-jvm permet de lancer deux jvm, chacune avec leurs composants, qui executent le projet de façon similaire au mono-jvm. 


Les lignes suivantes du fichier *config.xml* doivent être éditées : 
```
<host name="localhost"
      dir="/home/pablo/Dropbox/Cours/m2/alasca/ALASCA"/>
```
Il faut changer la chaine de caractère de l'attribut *dir*, qui doit être le chemin vers la racine du projet, sur votre machine. 

Les lignes suivantes du fichier *dcvm.policy* doivent être éditées : 
```
  permission java.io.FilePermission "/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/*", "read";
  permission java.io.FilePermission "/usr/lib/jvm/java-8-openjdk-amd64/lib/*", "read";
```
de manière à ce que les deux chemins pointent respectivement sur /jre/lib/* et /lib/* de votre jdk 8. 
les chemins présents ici sont ceux par défaut sous ubuntu pour openjdk. 

### lancement simple 

Pour lancer le projet en multi jvm, la méthode la plus simple est d'éxécuter la ligne de commande suivante depuis la racine du projet : 

```
java -ea -cp "jars/*" fr.sorbonne_u.components.cvm.utils.DCVM_Launcher config.xml
```

### lancement alternatif

On peut aussi lancer le projet en multi jvm en ouvrant quatre terminaux, tous placés à la racine du projet et en exécutant les commandes suivantes (une par terminal) : 


```
./start-cyclicbarrier
```

```
./start-gregistry
```

```
./start-dcvm jvm1
```

```
./start-dcvm jvm2
```
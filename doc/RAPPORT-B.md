# Rapport 

Le projet a pour but d'implémenter le jeu Citadelle pour des parties de 4 à 6 joueurs. 

## Point d'avancement

### Couverture du jeu

1. **Fonctionnalités**

2. **Fonctionnalités semaine de sprint** 
- Implémentation du richardBot 
- Fichier .csv pour enregistrer les statistiques d'une partie et on améliore ces statistiques à chaque nouvelle partie rejouée. 
- Utilisation de Jcommande et possibilité d'exécuter : `mvn exec:java "-Dexec.args=--demo` `mvn exec:java -Dexec.args="--csv"` 
3. **Ce qui reste à faire**
- La liberté des choix des actions à n'importe quel moment dans le tour du joueur
- Le choix d'utiliser ou non son pouvoir
- On ne gère pas les parties à 3 ou 7 joueurs 
- si le nombre de round est supérieur à 100 faire en sorte que la game se stuck

### Comparaison des bots implémentés

- **RichardBot**
<Br> Le richardBot a pour stratégie de rusher et de poser le plus de carte. Il réfléchit également au choix de son personnage avec l'état de la partie. 
- **SmartBot**
<Br> Le smartBot a pour stratégie de rusher et de poser le plus de cartes possible en un minimum de temps. 
- **RandomBot**
<Br>Le randomBot comme son nom l'indique, effectue ses choix aléatoirement tout au long de la partie. Il choisira aléatoirement son personnage et comment jouer les effets de son personnage. 
- **CustomBot**
<Br>Le customBot est notre botAgressif qui va s'occuper d'attaquer les joueurs bien positionnés mais n'accorde pas d'importante à son propre développement. 
- **BuilderBot**
<Br>Le builderBot va choisir ses personnages pour avoir et le maximum de ressource possible et poser le maximum de district possible. 



### Utilisation des logs 
En ce qui concerne les logs, nosu avons attribué à chaque player une couleur afin que la lecture de la partie soit lisible.
Les logs nous informe sur le début et la fin d'une partie, le début et la fin d'un round. nosua ffichon s également toutes les informations sur un joueur tel que sa main, sa citadelle, son pouvoir, son nombre d'or avant et après son tour. Enfin des logs affichant les stratégies et les effets des personnages existent.

### Utilisation des statistiques 


## Architecture et qualité 

### Architecture et sa justification 

Notre architecture est de type MVC. Nous avons dans : 
- model : les joueurs, les cartes, la banque, les roles 
- view : les logs
- controller : le fonctionnement du jeu global

Si à terme, nous voulions faire une interface, il aurait été plus facile de se retourner pour le faire. 
### Où trouver les informations importantes 

Les informations importantes concernant notre jeu et comment l'installer se trouve dans le readMe. La javadoc est également disponible afin de trouver l'utilité de chaque méthode implementer dans le code. 
### Qualité du code 

- **Parties bien optimisées**
coverage
sonarlint
- **Parties à refactor/améliorer**


### Répartition des tâches


### Organisation de l'équipe sur gitHub

- Stratégie de branche 
- Reviews



# Question 1 : Représentation binaire

Pourquoi est-il nécessaire de définir explicitement une convention telle que big-endian lorsqu’une structure est stockée dans un tableau de `byte` ? Que se passerait-il si `writeInt` utilisait big-endian mais `readInt` supposait little-endian sur une autre machine ?

A chaque échange la donnée serai corropu, quand l'un écrirai 255 en int l'autre lirait en int -16777216. 

# Question 2 : Signes et octets

Pourquoi une opération telle que `memory[offset] & 0xFF` est-elle importante lors de la reconstruction d’une valeur entière ? Expliquez le rôle de l’opérateur binaire en Java.

memory\[offset] est un byte, il prend donc une valeur de -128 à 127. De ce fait il semble impossible d'écrire une valeur de 0 à 255. On a donc choisit de l'écrire directement en bianaire (pour nous non signé) dans la variable, ce qui nous permet de passer outre le type. 
Pour passer en binaire on utilise 0xFF un masque et & qui nous permet de récupérer le byte en binaire. 

# Question 3 : Sérialisation

Pourquoi tester uniquement `readInt(writeInt(x)) == x` peut-il être insuffisant ? Et si oui comment sampler correctement les valeurs de x ? Expliquez en quoi l’inspection directe des octets permet de détecter davantage de classes d’erreurs.

Nos deux méthodes peuvent se comprendre sans pour autant écrire dans un format explicite (différence entre big endian et little endian par exemple). Un exemple concret peut être : des jumeaux qui lors de leur premières années se comprennent car ils ont un language a eux. Pour autant personne d'autres ne les comprend. 
Cela pose un problème si nos fichier doivent être lu par d'autres programmes.

# Question 4 : Bitmap

Pourquoi un bitmap est-il plus compact qu’une représentation utilisant un entier par bloc ? Exprimez la taille du bitmap en fonction du nombre de blocs.

Un entier (byte) prend un octet, or avec le bitmap on utilise un bit pour représenter un bloc. C'est donc 8 fois moins.
taille(Bitmap) = nb(Blocs) / 8

# Question 5 : Layout

Pourquoi les structures du système de fichiers doivent-elles occuper des zones mémoire déterministes ? Que se passerait-il si la position du bitmap changeait sans que les méthodes qui l’utilisent soient modifiées ?

Si des fichiers systeme sont créé au milieux des fichier classique, ils peuvent donc être facilement supprimé (ou écrasé) par n'importe qui, alors que s'ils sont dans une zone déterminé, il devient beaucoup plus facile de les protéger
Si par exemple on remplacait le bitmap par sont complémentaire, les méthodes écraserai les fichier déja créer et ne pourrait pas écrire sur les blocs vide. La modification du bitmap 'a la main' est donc assez risquée de ce point de vu.

# Question 6 : Inode

Pourquoi séparer les métadonnées du fichier de son contenu ? Expliquez pourquoi un inode peut être considéré comme une structure permettant de retrouver le contenu d’un fichier sans contenir directement ce contenu.

Les métadonnées indiquent comment sont construite les données (et donc comment les lire et les écrire). De ce fait on doit lire les métadonnées avant les données pour ne pas les interpréter n'importe comment.
Un inode stocke les metadonnées, ainsi que des pointeurs (directs et indirects). De ce fait la donnée n'est pas dans l'inode, l'inode indique juste ou la trouver et comment la lire.

# Question 7 : Allocation

Pourquoi `allocateBlock()` doit-il commencer à rechercher à partir du bloc `129` ? Que se passerait-il si la recherche commençait au bloc `0` ?

Théoriquement le bitmap est remplis de 1 jusqu'a 129. donc on perdrait juste du temps. Mais si jamais pour une raison X, un bit di bitmap passait a 0  dans l'intervale 0 - 129, le système de fichier pourrait écraser un inode, ce qui pourrait corrompre tout le système de fichier.

# Question 8 : Fragmentation

Deux systèmes peuvent-ils posséder exactement le même nombre de blocs libres mais présenter des niveaux de fragmentation différents ? Justifiez votre réponse avec une représentation sous forme de séquence de blocs libres et occupés.

Oui en effet on peut prendre un séquence du bitmap :
on considère que les deux bitmaps sont similaire en tout poit excepté de 130 à 136 ou on a :
0101010 et 1110000. 

# Wackamole 
## Descipción
Esta app creada con android studio en java, fue un proyecto del modulo de DAM.
## Contenido
- Conexion a una base de datos (no relaccional) en FireBase.
- Registro de usuario.
- Contenido de lifetime de las entidades.
- Registro global de marcador.
## Main Activities
### Registro
   <img src= "https://github.com/RodrigoHdezPimentel/Actividad_Desarrollo_Movil/blob/main/app/src/main/res/drawable/MainActivity.png">
### Juego
   <img src= "https://github.com/RodrigoHdezPimentel/Actividad_Desarrollo_Movil/blob/main/app/src/main/res/drawable/GameActivity.png">
### Podio
   <img src= "https://github.com/RodrigoHdezPimentel/Actividad_Desarrollo_Movil/blob/main/app/src/main/res/drawable/PodioActivity.png">
## Base de datos
La base de datos en firebase tiene un modelo muy sencillo. Hay un documento con los usuarios, con su id, nombre y contraseña y otro con las cuentas, donde se relacciona el id prodramáticamente para conseguir que un usuario tenga varias cuentas.\n
## Diseño
Para el diseño hemos elegido una paleta de colores llamativa, con verdes y azules vivos, con un diseño simple para usuarios de entre 7 y 15 años.
## EasterEggs
Una de las funciones más queridas por los "gamers", Wakamole presenta dos:
- En la pantalla de inicio, hay una animacion donde si clickas al topo, el martillo se la cae en la cabeza.
- La segunda, es el la pantalla de resumen de partida, donde si clicas el trofeo, se abre una Activity con el podio.

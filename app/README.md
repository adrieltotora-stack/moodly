Nombres 
- Ruberth Edgardo Tapara Hayqui
- Mathias Cardenas Concha
- Adriel Totora Vilca
  https://drive.google.com/drive/folders/1mdlJoTB89HUxLekl7JDezK1uNLLWbZsR?usp=drive_link
  Decisiones Técnicas Justificadas
1. Implementación de Room para la Persistencia Local
   Se seleccionó Room Persistence Library como motor de base de datos debido a que proporciona una capa de abstracción sobre SQLite que garantiza la integridad de los datos mediante la verificación de consultas en tiempo de compilación. Esta decisión permitió manejar la entidad MoodEntry de forma robusta, asegurando que el almacenamiento de niveles de ánimo, notas y fechas sea eficiente y menos propenso a errores de sintaxis SQL. Además, su integración nativa con Kotlin Coroutines facilita operaciones asíncronas para mantener la fluidez de la aplicación.

2. Arquitectura de Estado Reactivo con StateFlow
   Para el manejo del estado de la interfaz, se optó por transformar las consultas del DAO en flujos de datos reactivos utilizando stateIn dentro del MoodViewModel. Esta decisión técnica permite que la UI sea puramente declarativa: cualquier inserción en la base de datos se refleja automáticamente en la HomeScreen y el HistoryScreen sin necesidad de recargas manuales o llamadas adicionales a la red. El uso de la política WhileSubscribed(5000) optimiza el consumo de memoria al pausar la actualización de datos cuando el usuario no está viendo la pantalla.

3. Modelado de Datos y Gestión de Tipos Complejos
   Debido a que Room no admite colecciones de forma nativa para tipos no primitivos, se decidió modelar las etiquetas (tags) como una lista en la capa de lógica y almacenarlas como un String delimitado por comas mediante funciones de transformación. Esta decisión de aplanamiento de datos simplificó el esquema de la tabla mood_table sin comprometer la funcionalidad de la aplicación. De igual forma, se decidió utilizar el tipo Long para el manejo de fechas (System.currentTimeMillis()), garantizando una compatibilidad total con los operadores de ordenamiento de SQL.
# INICIO RÁPIDO: Windows PowerShell

## Ejecutar ConstrucTrack LogBook en 3 minutos

### Requisitos Previos

✓ Java 17 JDK (Eclipse Adoptium)
✓ Maven 3.8+
✓ Windows 10/11

---

## 4 PASOS

### PASO 1: Abrir PowerShell

```
Windows + R → "powershell" → Enter
```

### PASO 2: Navegar al Proyecto

```powershell
cd "C:\Users\57311\Documents\LAURA 2025 1\ANALISIS Y DESARROLLO DE SOFTWARE. (3070308)\PROYECTO\1. ConstrucTrack LogBook"
```

### PASO 3: Definir JAVA_HOME

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.15.6-hotspot"
```

### PASO 4: Ejecutar

```powershell
mvn clean package -DskipTests
mvn javafx:run
```

---

## ¿Qué Verás?

1. **Terminal**: Compilación con mensajes `[INFO]`
2. **Ventana**: Se abrirá la interfaz de ConstrucTrack
3. **Tiempo**: Primera vez: 10-15 minutos | Siguientes: 1-2 minutos

---

## Pantallazos Importantes

📷 **Captura 1**: Terminal con `mvn clean`
📷 **Captura 2**: Terminal con `BUILD SUCCESS`
📷 **Captura 3**: Ventana de ConstrucTrack abierta
📷 **Captura 4**: Terminal activo con `mvn javafx:run`

---

## Cerrar Aplicación

`Ctrl + C` en PowerShell

---

## Documentos Relacionados

- `INFORME_GA10_220501097_AA5_EV01.md` - Informe formal completo
- `GUIA_PRACTICA_PASO_A_PASO.md` - Detalles técnicos
- `README.md` - Índice general

# GUÍA PRÁCTICA: Despliegue Paso a Paso (Con Pantallazos)

## 🎯 Objetivo
Desplegar la landing page de ConstrucTrack LogBook en internet y documentar cada paso con capturas de pantalla para la evidencia GA10-220501097-AA6-EV01.

## ⏱ Tiempo Total: ~30 minutos
- Parte 1 (Freenom): 5 minutos
- Parte 2 (Netlify): 10 minutos
- Parte 3 (Integración): 5 minutos
- Parte 4 (Pruebas): 10 minutos

---

## 📋 LISTA DE VERIFICACIÓN PREVIA

Antes de comenzar, asegurate de tener:

- ✅ Navegador web actualizado
- ✅ Conexión a internet estable
- ✅ Email válido (te lo pedirán 2 veces)
- ✅ Herramienta de captura de pantalla (PrtScn o Snip Tool)
- ✅ Carpeta `recursos/` con `index.html`
- ✅ Documento de informe para pegar capturas

---

## 🚀 PARTE 1: CREAR DOMINIO EN FREENOM (5 min)

### Paso 1: Abrir Eu.org

1. Abre navegador
2. Ve a: **https://nic.eu.org**
3. Deberías ver página de inicio con opciones de registro

### Paso 2: Registrarse en Eu.org

1. Haz clic en **"Register"** (arriba a la derecha o en el banner)
2. Completa el formulario:
   - **Email:** Tu email personal
   - **Contraseña:** Mínimo 8 caracteres
   - **Nombre Completo:** Tu nombre
   - **País:** Selecciona tu país
3. Marca las casillas de términos
4. Haz clic en **"Register"**

**📷 CAPTURA 1: Pantalla de bienvenida Eu.org registrado**

### Paso 3: Solicitar Dominio

1. Login a tu cuenta Eu.org
2. En el dashboard, busca **"Register Domain"**
3. Ingresa un nombre (ej: `constructrack`)
4. El dominio será: `constructrack.eu.org`
5. Verifica disponibilidad
6. Si está disponible, haz clic en **"Register"**
7. Tendrás un mensaje de confirmación

**📷 CAPTURA 2: Confirmación de dominio registrado**

---

## 🌐 PARTE 2: PUBLICAR EN NETLIFY (10 min)

### Paso 1: Crear Cuenta Netlify

1. Abre navegador
2. Ve a: **https://www.netlify.com**
3. Haz clic en **"Sign Up"** (arriba a la derecha)
4. Elige método de registro:
   - **Recomendado:** GitHub (más fácil)
   - O: Google / Email

**Si usas Email:**
- Ingresa tu email
- Crea contraseña
- Verifica por email

**📷 CAPTURA 3: Dashboard de Netlify creado**

### Paso 2: Crear Nuevo Site (Drag & Drop)

1. En dashboard Netlify, busca **"Add new site"** o **"Create & deploy"**
2. Selecciona opción **"Deploy manually"** o **"Drag & drop"**
3. En la zona indicada, arrastra la carpeta `recursos/` (que contiene `index.html`)
4. O haz clic para buscar archivos

### Paso 3: Esperar Compilación

1. Verás una barra de progreso
2. Proceso típico: 10-30 segundos
3. Cuando termine, verás:
   - ✅ Checkmark verde
   - URL como: `randomname123456.netlify.app`

**📷 CAPTURA 4: Sitio deployado en Netlify (URL visible)**

### Paso 4: Acceder al Sitio

1. Haz clic en la URL generada
2. El navegador abrirá tu sitio en vivo
3. Verifica que carga completamente:
   - Header visible
   - Servicios se ven
   - Testimonios presentes
   - Footer visible

**📷 CAPTURA 5: Sitio en vivo en navegador (URL completa)**

---

## 🔗 PARTE 3: CONECTAR DOMINIO DE FREENOM (5 min)

### Paso 1: Nameservers de Netlify

En Netlify, debes obtener los nameservers:

1. En dashboard Netlify, busca **"Domain settings"** o **"Site settings"**
2. Haz clic en **"Add custom domain"**
3. Ingresa tu dominio Freenom (ej: `nexusweb.tk`)
4. Netlify mostrará nameservers (copiarlos):

```
dns1.p05.nsone.net
dns2.p05.nsone.net
dns3.p05.nsone.net
dns4.p05.nsone.net
```

**📷 CAPTURA 6: Nameservers mostrados en Netlify**

### Paso 2: Configurar Nameservers en Freenom

1. Ve a Freenom.com
2. Haz clic en **"My Domains"**
3. Selecciona tu dominio
4. Haz clic en **"Manage Domain"**
5. Busca sección **"Nameservers"**
6. Selecciona **"Use custom nameservers"**
7. Ingresa los 4 nameservers de Netlify
8. Haz clic en **"Save Changes"**

**📷 CAPTURA 7: Nameservers configurados en Freenom**

### Paso 3: Esperar Propagación

- Tiempo: 5-30 minutos (a veces más)
- Mientras esperas, continúa con pruebas

---

## ✅ PARTE 4: PRUEBAS Y VERIFICACIÓN (10 min)

### Paso 1: Verificar Conectividad

Después de 5-10 minutos:

1. Abre navegador nuevo
2. Ingresa: `https://tu-dominio.tk` (ej: `https://nexusweb.tk`)
3. Si funciona, verás tu sitio ✅
4. Verifica:
   - URL en barra (debe mostrar tu dominio)
   - Candado 🔒 (SSL activo)
   - Contenido completo visible

**📷 CAPTURA 8: Sitio accesible vía dominio personalizado**

### Paso 2: Prueba de Responsividad

1. Con el sitio abierto, presiona **F12** (Developer Tools)
2. Haz clic en ícono de dispositivo (móvil)
3. Selecciona dispositivos:
   - **iPhone 12** (375px)
   - **iPad** (768px)
   - Vuelve a **Desktop** (1920px)
4. Verifica que se adapta correctamente

**📷 CAPTURA 9: Sitio en mobile (375px)**
**📷 CAPTURA 10: Sitio en tablet (768px)**

### Paso 3: Verificar Elementos Funcionales

1. Haz clic en cada enlace del menú:
   - ✅ Servicios (scroll a sección)
   - ✅ Características (scroll a sección)
   - ✅ Testimonios (scroll a sección)
   - ✅ Contacto (scroll a sección)

2. Haz clic en botones CTA:
   - ✅ "Comienza Ahora" (scroll a contacto)
   - ✅ "Envíanos un Mensaje" (abre email)

**📷 CAPTURA 11: Menú navegacional funcional**

### Paso 4: Verificar Performance

1. Con DevTools abierto, ve a pestaña **"Lighthouse"**
2. Haz clic en **"Analyze page load"**
3. Espera 30-60 segundos
4. Busca puntuaciones:
   - Performance: ≥90
   - Accessibility: ≥85
   - Best Practices: ≥90
   - SEO: ≥90

**📷 CAPTURA 12: Reporte Lighthouse (puntuaciones)**

---

## 📸 LISTA DE CAPTURAS REQUERIDAS

Para el informe, necesitas mínimo estas capturas:

| # | Descripción | Ubicación en Informe |
|----|-----|--------|
| 1 | Dashboard Freenom registrado | Sección "Fase 1" |
| 2 | Dominio confirmado en Freenom | Sección "Fase 2" |
| 3 | Dashboard Netlify nuevo site | Sección "Fase 3" |
| 4 | Deploy exitoso Netlify | Sección "Fase 3" |
| 5 | Sitio en vivo (URL Netlify) | Sección "Fase 3" |
| 6 | Nameservers Netlify (para copiar) | Sección "Fase 3" |
| 7 | Nameservers configurados Freenom | Sección "Fase 3" |
| 8 | Sitio en vivo vía dominio personalizado | Sección "Fase 4" |
| 9 | Responsividad Mobile | Sección "Fase 4" |
| 10 | Responsividad Tablet | Sección "Fase 4" |
| 11 | Navegación funcional | Sección "Fase 4" |
| 12 | Lighthouse performance (verde) | Sección "Fase 4" |

---

## 🎯 RESUMEN RÁPIDO

```
1. FREENOM (5 min)
   ✅ Crear cuenta
   ✅ Buscar dominio
   ✅ Registrar (gratuito 12 meses)

2. NETLIFY (10 min)
   ✅ Crear cuenta
   ✅ Upload index.html
   ✅ Deploy automático
   ✅ Copiar nameservers

3. INTEGRACIÓN (5 min)
   ✅ En Freenom, pegar nameservers Netlify
   ✅ Esperar propagación

4. VERIFICACIÓN (10 min)
   ✅ Acceder vía dominio personalizado
   ✅ Probar responsividad
   ✅ Verificar elementos
   ✅ Capturar pantallazos

5. DOCUMENTACIÓN
   ✅ Pegar capturas en informe
   ✅ Completar análisis
   ✅ Exportar a PDF
```

---

## ⚠️ TROUBLESHOOTING

### El sitio muestra 404 o no carga

**Solución:**
1. Espera más tiempo para propagación (30-60 min)
2. Limpiar caché del navegador (Ctrl+Shift+Del)
3. Intentar desde navegador incógnito

### El dominio sigue mostrando Netlify.app

**Solución:**
1. Verifica que los nameservers estén correctos en Freenom
2. En Netlify, confirma que el dominio aparece como "Configured"
3. Espera 30 minutos y recarga

### SSL no está activo (sin 🔒)

**Solución:**
1. Espera a que Netlify genere certificado (automático)
2. Verifica en Netlify > HTTPS que sea "Active"
3. Si no, revisa configuración de nameservers

### Las imágenes no cargan

**Solución:**
1. Verifica que todos los archivos estén en carpeta
2. Comprueba rutas relativas en HTML
3. Re-deploy en Netlify

---

## ✨ RESULTADO FINAL ESPERADO

Después de seguir todos los pasos:

```
✅ Dominio personalizado: https://nexusweb.tk
✅ Sitio completamente publicado en internet
✅ Accesible desde cualquier dispositivo
✅ SSL/HTTPS activo
✅ Performance optimizado (Lighthouse >90)
✅ Responsivo (móvil, tablet, desktop)
✅ Menú navegacional funcional
✅ Documentación con capturas
✅ Informe completo para entregar
```

---

**¡Éxito! 🎉**  
Una vez completado este proceso, tendrás finalizada la evidencia GA10-220501097-AA6-EV01.

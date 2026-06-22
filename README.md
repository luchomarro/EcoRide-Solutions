# 🚲 EcoRide Solutions - Guía de Configuración del Sistema Distribuido

Este proyecto académico corresponde al curso **Soluciones Web y Aplicaciones Distribuidas** de la **UPN**. Se implementa bajo una arquitectura desacoplada utilizando un Frontend en Angular/Bootstrap y un Backend basado en REST APIs con Java Spring Boot y persistencia en MySQL, alineado al **ODS 9**.

---

## 🏗️ 1. Estructura Recomendada para GitHub
Para un control de versiones óptimo, clona tu repositorio vacío y organiza la raíz de la siguiente manera antes de realizar el primer `git commit`:

```text
ecoride-solutions/
├── .gitignore
├── README.md
├── frontend/
│   ├── Index-alquiler.html
│   └── Style.css
└── backend/
    ├── pom.xml
    ├── schema.sql
    └── src/
        └── main/
            ├── java/com/ecoride/api/...
            └── resources/application.properties
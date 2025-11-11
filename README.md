# 🛒 Ecommerce en Java Puro

**Descripción:**  
Proyecto e-commerce desarrollado en **Java**, utilizando **JSON** como sistema de persistencia. Permite manejar usuarios, productos, carritos de compra y órdenes de manera dinámica y segura.

---

## ⚡ Funcionalidades principales

- 👤 **Usuarios**: Registro y login de clientes (`Customer`) y administradores (`Admin`).  
- 🛍️ **Productos**: Agregar, eliminar, mostrar detalles personalizados sin exponer ID.  
- 🛒 **Carrito de compras**: Agregar productos, eliminar productos, calcular total, limpiar carrito.  
- 📝 **Órdenes**: Generación desde el carrito, control de stock, cálculo del total a pagar.  
- 💾 **Persistencia**: Datos almacenados en archivos JSON, con respaldo automático.  
- ⚠️ **Validaciones y excepciones**: Evita errores de ejecución y entradas inválidas.

---

## 🛠 Tecnologías

- Java 17  
- JSON (`org.json`) para persistencia  
- Colecciones de Java (`List`, `ArrayList`)  
- Programación Orientada a Objetos (POO)  

---

## 🎯 Objetivo del proyecto

Simular un sistema de **e-commerce funcional** dinamico, reforzando conceptos de:  
- POO  
- Manejo de colecciones y listas  
- Persistencia con JSON  
- Buenas prácticas de programación en Java  

---

## 📂 Estructura

<img width="1336" height="963" alt="estructura_tp_java" src="https://github.com/user-attachments/assets/632d6ec8-f966-4d53-a788-e9c31be0f108" />

## 🚀 Uso

1. Ejecutar la aplicación.  
2. Registrarse como cliente o administrador.  
3. Administrar productos y carrito de compras.  
4. Generar órdenes y revisar historial de pedidos.  

---

## 📌 Notas

- Los productos agregados al carrito se almacenan hasta generar la orden.  
- Se maneja stock y validaciones de entrada para asegurar consistencia de datos.  
- Las órdenes se generan automáticamente en archivos JSON con respaldo.

---

## 🗂️ Diagrama UML

<img width="1487" height="818" alt="uml_tp1_utn" src="https://github.com/user-attachments/assets/b00198c2-9e5b-417d-a1bb-4ceaf0e5a066" />


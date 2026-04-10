# Java Distributed Transaction (Reactive Microservices)

Este proyecto implementa un flujo de transacciones distribuidas utilizando una arquitectura orientada a eventos, diseñada bajo los principios de **Arquitectura Hexagonal (Clean Architecture)** y programación reactiva con **Spring WebFlux**.

## 🚀 Arquitectura del Sistema

El sistema consta de dos microservicios principales que se comunican de forma asíncrona a través de **Apache Kafka**:

1.  **Transaction Service**: Recibe las peticiones de transacciones, las registra con estado `PENDING` en PostgreSQL (R2DBC) y emite eventos de validación.
2.  **Antifraud Service**: Consume los eventos de transacción, aplica reglas de negocio para detectar fraude y emite un veredicto (`APPROVED` o `REJECTED`).

---

## 🛠️ Tecnologías Utilizadas

* **Java 17** & **Maven**
* **Spring Boot 3.x** (WebFlux)
* **Project Reactor** (Programación Reactiva)
* **Spring Data R2DBC** (PostgreSQL)
* **Apache Kafka** (Confluent Stack)
* **Docker & Docker Compose**

---

## 📋 Requisitos Previos

* **JDK 17** instalado.
* **Maven** 3.8 o superior.
* **Docker Desktop** funcionando.
* **IntelliJ IDEA** (recomendado).

---

## ⚙️ Pasos para levantar el proyecto

### 1. Levantar la Infraestructura
Primero, debemos levantar Kafka, Zookeeper y la base de datos PostgreSQL definidos en la raíz del repositorio.

```bash
docker-compose up -d
```
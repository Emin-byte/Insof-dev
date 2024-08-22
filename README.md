версия языка: Java 17 

Spring Boot version  3.*

сбор проекта через docker-compose.yml

DB: Postgres 

inTex: Spring Boot, Spring  MVC, Spring Data JPA , 
Spring Security (сбор ручной JWT ), 
Spring Cloud (OpenFeign для взаимодействий с другими сервисами,
Discovery Netflix Eureka для сервера регистрации) 

миграция: Liquibase 

для интерфейса: Thymeleaf html,css и немного инструменты JavaScript для клика и координации 

Архитектура: микросервисная и состоит из 5 сервисов 

dispatcher-service: Взаимодействует с пользователями и вернет нужные рендеренги и взаимодействует с другими микросервисами и т.д. 

можно  было ещё добавить Spring Cloud Getaway,
Cloud Config , Actuator , Circuit Breaker ,
Unit и Integration тесты но в требовании не увидел такие комплексы по этому не писал , 
могу тоже добавить если необходимо. 

проект запущен на Линукс VDS сервере 

ссылка на GitHub:
https://github.com/Emin-byte/Insof-dev

ссылка на сайт:
http://176.119.159.185:8080/

# ✅ DIGITOPIA Case — Backend TODO List

## 🧩 1️⃣ Tamamlananlar

- [x] **user-service**
    - [x] CRUD işlemleri
    - [x] PostgreSQL + Flyway migration
    - [x] RabbitMQ konfigürasyonu
    - [x] Swagger & Actuator

- [x] **org-service**
    - [x] CRUD işlemleri
    - [x] PostgreSQL + Flyway migration
    - [x] RabbitMQ bağlantısı
    - [x] Swagger & Actuator

- [x] **invitation-service**
    - [x] CRUD (create / accept / decline)
    - [x] PostgreSQL + Flyway migration
    - [x] RabbitMQ event publish (`invitation.created`)
    - [x] Swagger & Actuator

---

## ⏳ 2️⃣ Zorunlu (Must Have) Kalan Görevler

- [ ] **Docker Compose yapılandırması**
    - [ ] `postgres`, `rabbitmq`, `user-service`, `org-service`, `invitation-service`
    - [ ] Ortak `backend` network oluştur
    - [ ] Servis environment değişkenlerini `.env` ile bağla
    - [ ] Her servis için `depends_on` ekle

- [ ] **Servisler arası REST iletişimi**
    - [ ] `org-service` içinde `ownerId` doğrulaması için `user-service`’e REST çağrısı ekle
    - [ ] (Opsiyonel) `invitation-service` içinde `org-service` ve `user-service` kontrolü ekle

- [ ] **Global Exception Handler**
    - [ ] Tüm servislerde standart JSON hata formatı:  
      `{ "error": "", "message": "", "timestamp": "" }`

- [ ] **DTO ve Mapper yapısı**
    - [ ] Entity’leri doğrudan dönmek yerine DTO kullan (`UserResponse`, `OrganizationResponse`, `InvitationResponse`)
    - [ ] ModelMapper veya manuel dönüşüm ekle

- [ ] **README.md (ana dizine)**
    - [ ] Kurulum adımları (`docker compose up --build`)
    - [ ] Servis URL’leri (`8081`, `8082`, `8083`)
    - [ ] Swagger erişim linkleri
    - [ ] API örnekleri (curl veya Postman)
    - [ ] Ortam değişkenleri (.env örnekleri)
    - [ ] Proje mimarisi diyagramı (Mermaid / Draw.io)

---

## 🌟 3️⃣ Bonus (Optional / Artı Puan)

- [ ] **RabbitMQ Event Tüketimi**
    - [ ] `org-service` → `invitation.created` event’ini dinlesin
    - [ ] Basit loglama veya notification kaydı

- [ ] **Basit Auth Mekanizması**
    - [ ] `user-service` üzerine Basic Auth veya JWT ekle
    - [ ] `created_by` ve `updated_by` alanlarını aktif hale getir

- [ ] **Audit / Log Service**
    - [ ] Yeni servis (örnek: `audit-service`) RabbitMQ event’lerini dinlesin
    - [ ] `invitation.created`, `invitation.accepted` loglasın

- [ ] **Integration Testler**
    - [ ] `@SpringBootTest` + H2 kullanarak CRUD testleri ekle

- [ ] **Mermaid veya Draw.io Diyagramları**
    - [ ] Entity-Relation Diagram
    - [ ] Sequence Diagram (invitation flow)

- [ ] **CI/CD Pipeline (opsiyonel)**
    - [ ] GitHub Actions → build + test
    - [ ] Docker image push (Docker Hub)

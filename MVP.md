## 🧩 Digitopia Case – MVP Yapılacaklar Listesi

- [x] Swagger / OpenAPI ekle (her servise)  
  *springdoc dependency + `/swagger-ui.html` erişimi sağla.*

- [x] (Opsiyonel) `.http` veya Swagger Collection dosyası ekle  
  *API testlerini kolaylaştırmak için.

- [x] Global Exception Handler (`@ControllerAdvice`) ekle  
  *400 (Bad Request) ve 404 (Not Found) hatalarını JSON formatında dön.*

- [x] Healthcheck endpoint’lerini doğrula  
  *Docker Compose’daki yollar `/api/v1/.../healtz` ile uyumlu olsun.*

- [x] Flyway sürümleme disiplini koru  
  *`V1__` dosyaları sabit, yeni değişiklikleri `V2__`, `V3__` olarak ekle.*

- [ ] RabbitMQ mesajlarını test et  
  *`invitation.created` ve `invitation.status.changed` event’leri “digitopia.exchange” üzerinde görünmeli.*

- [ ] Manuel E2E test akışı oluştur  
  *User → Org → Invitation oluştur; aynı kombinasyonda ikinci POST → 400 beklenir.*

- [ ] (Opsiyonel) Scheduler ekle  
  *7 gün sonra `PENDING` davetleri `EXPIRED` durumuna geçir.*

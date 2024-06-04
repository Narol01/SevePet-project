## Настройка проекта

* Необходимо обеспечить наличие Environment Variables
  * локально:
    * `spring.datasource.username`
    * `spring.datasource.password`
    * `spring.datasource.url`
  * через Digital Ocean:
    * для базы данных 
      * `DB_HOST`
      * `DB_PORT`
      * `DB_NAME`
      * `DB_USERNAME`
      * `DB_PASSWORD`
    * если отправляем письма через base_url (у нас нет)
      * `base_url` 
      * `spring_mail_password`
      * `spring_mail_username`
    * для загрузки файлов (работа с Digital Ocean/AWS S3)
      * `s3_accessKey`
      * `s3_endpoint`
      * `s3_region` 
      * `s3_secretKey`
    
    

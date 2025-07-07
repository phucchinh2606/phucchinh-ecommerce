# file application.yaml (hoặc .yml) tôi không push lên bởi chứa key aws. các bạn tham khảo dưới đây

server:
  port: dat 4 so bat ky (vd 1234)

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/{tendatabase}
    username: root
    password: { password database }
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update

secretJwtString: { len generate random org, vao encryption key tao chuoi bat ki nem vao day la duoc }

aws:
  s3:
    access: { key nay bi mat ko duoc lo }
    secret: { cung bi mat }


clone repo về thì thêm file application.yaml (hoặc .yml) vào src/main/resources/ rồi theo cấu hình như trên.

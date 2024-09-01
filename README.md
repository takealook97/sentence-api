# Random Sentence API

## based on

- traditional korean proverbs, more than 4,000
- famous saying (soon)
- maxim (soon)

---

## direction

### api url

💡`https://sentence.udtk.site/random/{count}`

- count : number of sentences to get
- count range : 1 ~ 20

### response example

`https://sentence.udtk.site/random/2`

```json
[
  {
    "author": null,
    "content": "쌀은 쏟고 주어도 말은 하고 못 줏는다."
  },
  {
    "author": null,
    "content": "찬 소리는 무덤 앞에 가 하여라"
  }
]
```

### request limit

- 20 requests per 5 minutes
- when the limit is exceeded, the response will be `429 Too Many Requests`
    - time left to reset the limit will be included in the response header `X-Rate-Limit-Retry-After-Seconds`

---

## stack

- Java 17
- Spring Boot 3.3.3
- Spring Data JPA
- bucket4j
- MariaDB
- Redis
- Docker
- Nginx
- Jenkins
- Proxmox

---

## architecture

<img src="./assets/sentence-architecture.jpg" alt="architecture">
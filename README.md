# Random Sentence API

## 🗂️ Data Source

- Traditional Korean proverbs, more than 4,000
    - from [KRPia](https://www.krpia.co.kr/product/main?plctId=PLCT00004626#none)
- Quotes, more than 200 (more to be added)
    - from [Goodreads](https://www.goodreads.com/quotes) (soon)
- Maxim (soon)

---

## 📚 Specification

(Author information is not provided for proverbs and common sentences.)

| Endpoint                                                     | Description                                      | Request Parameters                                                                                                                                   | Default Values                  | Range           |
|--------------------------------------------------------------|--------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------|-----------------|
| `https://sentence.underthekey.com/{id}`                      | Returns a sentence by ID.                        | None                                                                                                                                                 | None                            | None            |
| `https://sentence.underthekey.com/random?count=`             | Returns several random sentences.                | `count`: Number of sentences to get.                                                                                                                 | `count`: 1                      | `count`: 1 ~ 20 |
| `https://sentence.underthekey.com/language?language=&count=` | Returns random sentences in the chosen language. | `language`: `kor`, `eng`<br>`count`: Number of sentences to get.                                                                                     | `language`: `kor`<br>`count`: 1 | `count`: 1 ~ 20 |
| `https://sentence.underthekey.com/sort?sort=&count=`         | Returns random sentences in the chosen sort.     | `sort`: `가1`, `가2`, `나`, `다`, `마`, `바`, `사1`, `사2`, `아1`, `아2`, `자1`, `자2`, `차`, `카`, `타`, `파`, `하`, `quote`<br>`count`: Number of sentences to get. | `sort`: `가1`<br>`count`: 1      | `count`: 1 ~ 20 |

### Response Example

ex) `https://sentence.underthekey.com/random?count=2`

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

### ❗️Request Limit

- **<U>20 requests per 5 minutes</U>**
- When the limit is exceeded, the response will be `429 Too Many Requests`
- Time left to reset the limit will be included in the response header `X-Rate-Limit-Retry-After-Seconds`

---

## 🛠️ Stack

- Java 17
- Spring Boot 3.3.3
- Spring Data JPA
- bucket4j
- MariaDB
- Redis 7.2
- Proxmox
- Nginx
- Docker
- Jenkins

---

## 🏗️ Architecture

<img src="./assets/sentence-architecture.jpg" alt="architecture">

---

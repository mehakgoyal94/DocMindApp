# DocMind RAG Java

An AI-powered knowledge assistant built with Java, Spring Boot, Spring AI, OpenAI, and Qdrant.

DocMind ingests documents, chunks text, creates embeddings, stores vectors, retrieves relevant context, and generates grounded answers with source citations.

**DocMind v1**

Spring Boot
Spring AI
Qdrant
OpenAI embeddings
OpenAI chat model
REST API only
Docker Compose
citations in every answer


--------------------------------------------------------------------*Qdrant vs FAISS*---------------------------------------------------------------------
⚔️ Qdrant vs FAISS — Core difference
🧠 FAISS
A library (not a service)
Developed by Meta
Runs inside your application
Does *vector similarity search* only

👉 Think:

“fast math engine for vectors”

🧱 Qdrant
A full vector database (service)
Runs as a separate system
Has *APIs, persistence, filtering, indexing*

👉 Think:

“database built specifically for embeddings”
Example:
`{
"must": [
{ "key": "documentType", "match": { "value": "runbook" } }
]
}`

“Why did you choose Qdrant over FAISS?”

“FAISS is a high-performance vector similarity library, but it lacks persistence, metadata filtering, and service-level capabilities.
Since I was building a production-style RAG system, I chose Qdrant because it provides a full vector database with filtering, 
APIs, and integration with Spring AI, which allowed me to focus on system design instead of building infrastructure around FAISS.”



----------------------------------------------------------*RAG PROPERTIES* ---------------------------------------------------------------------

Defined RAG properties-
chunk-size → what you store  
chunk-overlap → how well context is preserved  
top-k → how much you retrieve  
min-score → similarity threshold for a chunk to be considered relevant

📦 Example

Document: 10,000 characters
With chunk-size: 900 → ~11 chunks
chunk-overlap: 150 -> [0–900],[750–1650]
top-k: 5
min-score: 0.70(Chunk A → 0.82 ✅
Chunk B → 0.75 ✅
Chunk C → 0.65 ❌ (filtered out))


These parameters are part of retrieval tuning. 
In production systems, we usually experiment with chunk size, overlap, top-k, and similarity thresholds to balance precision, recall,
latency, and cost. Sometimes we also add re-ranking on top of retrieval.



----------------------------------------------------------*GETTING STARTED* ---------------------------------------------------------------------

**Prerequisites:** Java 21, Maven, Docker, and an OpenAI API key.

1. Start Qdrant (vector store):
   ```
   docker compose up -d
   ```
2. Provide your OpenAI API key as an environment variable:
   ```
   export OPENAI_API_KEY=sk-...
   ```
3. Run the application (serves on http://localhost:8080):
   ```
   mvn spring-boot:run
   ```

**Supply your own document.** No sample document is included in this repository.
Bring any document of your own (PDF, DOCX, TXT, etc.) and ingest it:
```
curl -F "file=@/path/to/your-document.pdf" http://localhost:8080/documents/upload
```
Then ask questions grounded in that document:
```
curl -X POST http://localhost:8080/chat/query \
  -H "Content-Type: application/json" \
  -d '{"question": "What is this document about?"}'
```
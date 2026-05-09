from fastapi import FastAPI
from pydantic import BaseModel
import requests
import json

app = FastAPI()

class PromptInput(BaseModel):
    text: str

@app.post("/emotion_parse")
def emotion_parse(input: PromptInput):
    print("REQUEST RECEIVED")
    print(input.text)
    try:
        response = requests.post(
            "http://127.0.0.1:11434/api/generate",
            json={
                "model": "gemma:2b",
                "prompt": 
                f"""Return ONLY valid JSON in this exact format:{{"emotion": "<emotion>","advice": "<one line advice>"}}Input:{input.text}""",
                "stream": False
            }
        )
        result = response.json()
        raw = result.get("response", "{}")
        parsed = json.loads(raw)

        return {
            "emotion": parsed.get("emotion", "UNKNOWN"),
            "advice": parsed.get("advice", "(No response)")
        }
    except Exception as e:
        return {
            "Advice": f"Error: {str(e)}"
        }
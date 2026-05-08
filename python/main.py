from fastapi import FastAPI 
from pydantic import BaseModel 
import requests 
app = FastAPI() 
class PromptInput(BaseModel): 
    text: str 
@app.post("/emotion_parse") 

def emotion_parse(input: PromptInput): 
    try: 
        response = requests.post( 
            "http://127.0.0.1:11434", 
            json={ 
            "model": "gemma:2b", 
            "prompt": f"Write a one line response of advice and emotion displayed :{input.text}",
            "stream": False 
            } 
        ) 
        result = response.json() 
        return {"Advice": result.get("response", "(No response)")} 
    except Exception as e: 
        return {"Advice": f"(Error: {str(e)})"} 
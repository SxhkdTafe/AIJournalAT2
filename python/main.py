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
            "http://localhost:11434/api/generate", 
            json={ 
            "model": "gemma:2b", 
            "prompt": input.text,
            "stream": False 
            } 
        ) 
        result = response.json() 
        return {"Advice": result.get("response", "(No response)")} 
    except Exception as e: 
        return {"Advice": f"(Error: {str(e)})"} 
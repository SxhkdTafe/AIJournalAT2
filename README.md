# AIJournalAT2
---
## Run Backend
Paste Command in directory of python script 
> python -m uvicorn main:app --host 0.0.0.0 --port 8000
---
## Run FrontEnd
Click Run app button on android studio or input 
>Shift + F10
---
# Ollama Setup
Click link and follow setup steps
>https://ollama.com/download/OllamaSetup.exe

Open Windows PowerShell and paste the following command
>ollama pull gemma:2b

Navigate to directory of python file (main.py) and paste into terminal
> pip install fastapi uvicorn requests
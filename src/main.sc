require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /


    state: Start
        q!: $regex</start>
        intent!: /Старт
        go!: /Start/Registration
        
        
        state: Registration
            a: Приветики, мой дорогой друг! Я знаю, ты очень переживаешь из-за того, что не знаешь, что ждет торговлю в такое непростое время
            buttons:
                "Да, очень" -> /Start/Registration/YouCanDoIt
        
        
            state: YouCanDoIt
                a: Поверь мне, были уже такие времена и наши стойкие коллеги предприниматели смогли преодолеть трудности и многие взлетели высоко до звезд. И ты тоже сможешь!
                buttons:
                    "Да! Смогу!" -> /Start/Registration/GetName
                    
                state: ClickButtons
                    q: *
                    a: Нажмите, пожалуйста, кнопку.
                    go!: .. 


            state: GetName
                a: А теперь давай оставим разговоры о тревоге и познакомимся. Меня зовут Каваюша. Я бот Молоковой Анны, лучшего учителя по работе с китайскими поставщиками. Я буду твоим верным помощником. Как тебя зовут?
                go!: ./ReceiveName
                 
                state: ReceiveName
                     
                    state: CorrectName
                        q: * @pymorphy.name *
                        script: $client.name = $parseTree.value
                        go!: ./Compliment
        
                        state: Compliment
                            a: Какое классное имя, {{ $client.name }}! Человек с таким именем рожден быть успешным!
                            buttons:
                                "Взаимно!" -> /Start/Registration/GetMail
                                
                            state: ClickButtons
                                q: *
                                a: Нажмите, пожалуйста, кнопку.
                                go!: ..    
        
                    state: IncorrectName
                        event: noMatch
                        a: Не балуйтесь. Напишите ваше имя
                        go!: /Start/Registration/GetName/ReceiveName
        
        
        
            state: GetMail
                a: {{ $client.name }}, напиши свой email, чтобы я мог присылать тебе полезные материалы и анонсы о предстоящих интересных событиях. Обещаю не спамить!
                go!: ./ReceiveMail
                state: ReceiveMail
                    state: CorrectMail
                        q: * @duckling.email *
                        script: 
                            $client.mail = $parseTree.value;
                        go!: /Start/Registration/GetPhone
                
                    state: IncorrectMail
                        event: noMatch
                        a: Мне кажется email введен с ошибкой, проверь пожалуйста и введи снова
                        go!: /Start/Registration/GetMail/ReceiveMail
                
                
            state: GetPhone
                a: Отлично, напиши твой номер телефона, я тебе позвоню, чтобы поболтать! Я шучу, мне нужно для занесения в мою систему
                go!: ./ReceivePhone
                
                state: ReceivePhone
                    a: ReceivePhone
                    
                    state: CorrectPhone
                        a: CorrectPhone
                        q: * @duckling.phone-number *
                        script: 
                            $client.phone = $parseTree.value;
                            var user = $request.userFrom['id']
                            $session.user = user
                        GoogleSheets:
                            operationType = writeDataToLine
                            integrationId = a4fd69df-aa3f-4e92-9ee6-e082fede18fb
                            spreadsheetId = 1A_sPgTWNXf9SImudDGystPrXwbNo8Z2gvEzkkkfygnI
                            sheetName = 1
                            body = {"values":["{{ $session.user }}", "{{ $client.name }}", "{{ $client.phone }}", "{{ $client.mail }}"]}
                            okState = /Start/Registration/FinishRegistration/LastPhrase/GoodBye
                            errorState = /Start/Registration/FinishRegistration/LastPhrase/GoodBye
                        
                        go!: /Start/Registration/FinishRegistration
                
                    state: IncorrectPhone
                        event: noMatch
                        a: Мне кажется телефон введен с ошибкой, проверь пожалуйста и введи снова
                        go!: /Start/Registration/GetPhone/ReceivePhone
                
                
            state: FinishRegistration
                a: Все получилось! Теперь держи твой подарочек – список востребованных в кризис ниш
                script: $reactions.inlineButtons({text:"Получить подарок", url: "https://705402.selcdn.ru/bot_storage/2/Anticrisis_goods.pdf"});
                go!: ./LastPhrase
                
                
                state: LastPhrase
                    a: И скажи себе: Я не сдамся ни перед какими трудностями!


            
    state: NoMatch || noContext = true
        event!: noMatch
        a: Каваюша не понимает :( Введи /start чтобы начать заново

    state: Obscene
        q!: * @mlps-obscene.obscene *
        a: Ненормативная лексика. Начнем заново
        go!: /Start/Registration
            
            

require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /


    state: Start
        q!: $regex</start>
        intent!: /Старт
        go!: /Start/Registration
        
        
        state: Registration
            a: Приветики, мой дорогой друг! Я знаю, ты очень переживаешь из-за того, что не знаешь, что ждет торговлю в такое непростое время
            inlineButtons:
                "Да, очень" -> ./YouCanDoIt
        
        
            state: YouCanDoIt
                a: Поверь мне, были уже такие времена и наши стойкие коллеги предприниматели смогли преодолеть трудности и многие взлетели высоко до звезд. И ты тоже сможешь!
                inlineButtons:
                    "Да! Смогу!" -> /Start/Registration/GetName


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
                            inlineButtons:
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
                        go!: /Start/Registration/ReceiveMail
                
                
            state: GetPhone
                a: Отлично, напиши твой номер телефона, я тебе позвоню, чтобы поболтать! Я шучу, мне нужно для занесения в мою систему
                
                
                
                
                
                
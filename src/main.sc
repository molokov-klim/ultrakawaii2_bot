require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /


    state: Start
        q!: $regex</start>
        intent!: /Старт
        go!: /Start/Registration
        
        
        state: Registration
            a: Здравствуйте! Я – бот-помощник в компании Moanna Yiwu Trading. Я помогу вам с оформлением заявки на закупку товаров из Китая и доставку грузов.
               Пожалуйста, заполните данные:
            go!: /Start/Registration/GetName
                   
            state: GetName
                a: напишите ваше имя
                go!: ./ReceiveName
                state: ReceiveName
                    state: CorrectName
                        q: * @pymorphy.name *
                        script: $client.name = $parseTree.value
                        go!: /Start/Registration/GetPhone
        
                    state: IncorrectName
                        event: noMatch
                        a: Имя не распознано. Напишите имя
                        go!: /Start/Registration/GetName/ReceiveName
        
            state: GetPhone
                a: напишите ваш телефон
                go!: ./ReceivePhone
                state: ReceivePhone
                    state: CorrectPhone
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
                            okState = /Start/Registration/FinishRegistration
                            errorState = /Start/Registration/FinishRegistration
                
                    state: IncorrectPhone
                        event: noMatch
                        a: Мне кажется телефон введен с ошибкой, проверьте пожалуйста и введи снова
                        go!: /Start/Registration/GetPhone/ReceivePhone
                        
                        
            state: GetMail
                a: напишите ваш email
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
                
                
            state: FinishRegistration
                a: Все получилось! Теперь держи твой подарочек – список востребованных в кризис ниш
                script: 
                    $reactions.inlineButtons({text:"Получить подарок", url: "https://705402.selcdn.ru/bot_storage/2/Anticrisis_goods.pdf"});
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
            
            

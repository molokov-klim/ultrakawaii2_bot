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
                a: getName
        
        
        
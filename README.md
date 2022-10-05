# Search_engine
Приложение, которое позволяет индексировать страницы и осуществлять по ним быстрый поиск. 
Движок разрабатывается на фреймворке Spring:
обходит все страницы сайта начиная с главной;
индексирует страницы сайта — подсчитывая слова на страницах сайта и по поисковому запросу определять наиболее релевантные (соответствующие поисковому запросу) страницы;
реализована система поиска информации с использованием созданного поискового индекса.\n
Для работы приложения необходимо, установить на компьютер PostgreSQL для хранения данных проиндексмированных сайтов.
В application.properties указать ваши учетные данные:
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
А также список сайтов для индексации через запятую
Например 
path.page.all2=https://www.adrsnab.ru, http://www.playback.ru, http://radiomv.ru

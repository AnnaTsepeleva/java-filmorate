# java-filmorate
Template repository for Filmorate project.
![ER DIAG](diagram.png)

1. Получить фильм по ID
select * 
from films
where films.id = ID
2. Получить все фильмы
select * 
from films
3. Получить фильмы топ 10 фильмов по лайкам
select  f.name, count(fl.film_id)
from films as f
left join film_likes as fl on fl.film_id = f.id
group by f.name
order by count(fl.film_id)
limit 10
4. Получить фильмы, которые понравились пользователю userID
select  f.name
from films as f
left join film_likes as fl on fl.film_id = f.id
where fl.user_id = userID
5. Получить потзователя по ID
select * 
from users
where users.id = ID
6. Получить всех пользователей
select *
from users
7. Получить друзей пользователя userID
select u.*  //отправленные заявки
from users as u
left join friendship as fs on  friendship.user_id=u.id
where u.id = userID and u.status = true
union
(select f.* //принятые заявки
from users as f
left join friendship as fs on  friendship.friend_id=f.id
where f.id = userID and fs.status = true)


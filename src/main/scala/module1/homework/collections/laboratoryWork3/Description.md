# Необходимо будет "проверить" формулу вероятности
https://www.matburo.ru/tvbook_sub.php?p=par15
## Если кратко:
В урне 3 белых и 3 черных шара. Из урны дважды вынимают по одному шару, не возвращая их обратно. Найти вероятность появления белого шара
## Как будем делать:
1.	создать класс с моделированием эксперимента, в нем должна быть коллекция (List) моделирующая урну с шариками (1 - белый шарик, 0 - черный шарик) и функция случайного выбора 2х шариков без возвращения (scala.util.Random), возвращать эта функция должна true (если был выбран белый шар) и false (в противном случае)
2.	создать коллекцию объектов этих классов, скажем 10000 элементов, и провести этот эксперимент (функция map)
3.	посчитать количество элементов массива из пункта 2 где функция вернула true, это количество поделенное на общее количество элементов массива

**PS:** чем больше будет количество опытов в пункте 2, тем ближе будет результат моделирования к аналитическому решению

## Критерии оценки:
1.	Результат должен быть близок к ожидаемому
2.	Использовать как можно меньше явных циклов, отдавая предпочтение высокоуровневым функциям, как то map, foreach, fold, filter
3.	Используйте только стандартные библиотеки из базового набора
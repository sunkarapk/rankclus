all:
	java -cp ./bin/:./lib/jama.jar:. RankClus 2 95 10

install:
	javac -cp ./src/:./lib/jama.jar:. src/*.java
	java -cp ./src/:./lib/jama.jar:. RankClus 2 95 10

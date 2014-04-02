rm -f lan? hout? hin?
rm -f rout?
javac Host.java
javac Router.java
javac Controller.java
java Host 0 0 sender 50 20&
java Host 1 1 receiver &
java Router 0 0 1 &
java Router 1 1 2 &
java Router 2 2 3 &
java Router 3 3 0 &
java Controller host 0 1 router 0 1 2 3 lan 0 1 2 3&


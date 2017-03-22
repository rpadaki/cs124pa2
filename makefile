all: MatrixMultiply.class
%.class: %.java
	javac $<
clean:
	rm -f *.class
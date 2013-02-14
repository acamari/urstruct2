srcdir=src
#csrc=`echo srcdir/*.c`
#cobj=${csrc:%.c=%}
jsrc=`echo ./*.java`
jobj=${jsrc:%.java=%.class}
JAVACFLAGS=-encoding UTF-8
regin=`echo regress/*.in`
regout=${regin:%.in=%.out}

all:V: $jobj #$cobj

regress:V: $regout

#%: %.c
#	cc -Wall -o $target $prereq

%.class: %.java
	javac $JAVACFLAGS $prereq

regress/%.out:VQ: regress/%.in
	printf "==> mk %-40s" $target"..."
	prog=`expr "$stem" : '\([^.]\{1,\}\)'`
	java $prog < $prereq > $target
	cmp -s $target regress/$stem.exp && echo ok || echo fail 
	true

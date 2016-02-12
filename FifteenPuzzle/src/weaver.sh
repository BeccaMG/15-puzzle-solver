file=$1
if [ "$file" == "" ] ; then
	echo "Error: Missing parameter, Example: ./weaver.sh file.java"
	exit 1
fi
#######################################################################################

fileinmem=$(cat $file)
lines=$(cat $file | wc -l)
i=1
funcname=""
funcnum=$(echo "$fileinmem" | grep "public" | grep -v class | wc -l)
#Search for functions and constructors, add the var++ to each function and store the function in a list
while [ $i -le $funcnum ] ; do
	line=$(echo "$fileinmem" | grep -v class | grep -m $i "public" | tail -n 1)
	num=$(echo "$line" | cut -d'(' -f 1 | wc -w)
    funcname=$(echo "$line" | cut -d'(' -f 1 | awk '{print $'$num'}')
    funcname="$(tr '[:upper:]' '[:lower:]' <<< ${funcname:0:1})${funcname:1}" #Force lower case for variable names
	funclist=$(echo -e "$funclist\n$funcname")
	match=$line
	#insert='\t\t'$funcname'_Counter++;'
	sed -i "s/$match/$match\n$insert/" $file	
	let i++
done

funclist=$(echo "$funclist" | grep -v '^$' | uniq)
funcnum=$(echo "$funclist" | wc -l)
i=1
#Go through function list to add the var the declaration at the begining of the class
while [ $i -le $funcnum ] ; do
	varname=$(echo "$funclist" | head -n $i | tail -n 1)
	match=$(echo "$fileinmem" | grep "public class")
	#insert='\tpublic static int '$varname'_Counter = 0;'
	#echo "$i $insert"
	sed -i "s/$match/$match\n$insert/" $file	
	let i++
done

#grep -r "public static void main" * ../ | grep -v weaver
#Search for the main class for this project

fullpath=$(readlink -f $file)
i=0
dirdep=5
while [ $i -le $dirdep ] ; do
found=false
dir=$(dirname $fullpath)
maindir=$(grep -r "public static void main" $dir | grep -v weaver | wc -l)
if [ $maindir -eq 1 ] ; then
	maindir=$(grep -r "public static void main" $dir | grep -v weaver | cut -d':' -f 1)
	found=true
	break
fi
fullpath=$dir
let i++
done

if [ "$found" == "false" ] ; then
	echo "Error: File of main class not found"
	exit 1
fi
classname=$(echo "$fileinmem" | grep "public class" | awk '{print $3}')
fileinmem=$(cat $maindir)
lines=$(echo "$fileinmem" | wc -l)
i=$(echo "$fileinmem" | grep -m 1 -n "public static void main" | cut -d':' -f 1)
instance=$(echo "$fileinmem" | grep -v import | grep -v '//' | grep new | grep -m 1 $classname | cut -d'=' -f 1 | rev | awk '{print $1}' | rev)
if [ "$instance" == "" ] ; then
	echo "Instance of class not defined in main class"
	exit 1
fi
braces=0
while [ $i -le $lines ] ; do
	line=$(echo "$fileinmem" | sed ''$i'q;d')
	open=$(echo $line | grep "{" | wc -l)
	close=$(echo $line | grep "}" | wc -l)
	if [ $open -eq 1 ] ; then let braces++ ; fi
	if [ $close -eq 1 ] ; then let braces-- ; fi	
	if [ $braces -eq 0 ] ; then	break ;	fi
	let i++
done
a=1
#Go through function list to add the prints to the main class
while [ $a -le $funcnum ] ; do
	varname=$(echo "$funclist" | head -n $a | tail -n 1)
	#insert='System.out.println("Function_'$varname':"+'$instance'.'$varname'_Counter);'
	echo 'System.out.println("Function_'$varname':"+'$instance'.'$varname'_Counter);'
	sed -i ''$i'i '$insert'' $maindir
	let a++
done

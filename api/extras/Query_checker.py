#MODULES REQUIRED FOR THIS SCRIPT:
#1 - pymysql
#2 - lxml

import pymysql
from lxml import etree
#modify here your db info:-
conn = pymysql.connect(host='127.0.0.1',user='root', passwd="root", db='ddu_sdmx_new_24082012')
cur = conn.cursor()
#path to codes.xml file
tree = etree.parse("E:/Company/Automated_test/codes.xml")
root = tree.getroot()
i=0
count=0
data={}
notWorkingDeList=[]
workingList=[]
#CHANGE DATE HERE IF YOU WANT:
startDate="2012-9-12"
endDate="2012-9-12"


num=root[i]
elements = tree.xpath('//queries/node()[text()]')

for text in root.iter():
    #print ('**************')
    #print (text.items())
    dat = text.items()
    i=i+1
    try:
        query = text.text
        query=query.replace(":startDate",startDate)
        query=query.replace(":endDate",endDate)
       # print(query)
   
        result = cur.execute(query)
       # print("query result=")
        workingList.append(dat)
        #for r in cur:
            #print(r)
    except :
        print("query is not right DE=",dat);
        count=count+1
        notWorkingDeList.append(dat)

  #  data.append([text.items,text.text])
    
cur.close()
conn.close()

print("")
print("Following DE's have problem:")
print(notWorkingDeList)
print("total query=",i,"query with problems=",count)
#print(workingList)

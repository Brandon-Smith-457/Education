import numpy as np
np.set_printoptions(threshold=np.inf)
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

import DataAnalyze as dataAnalyze


def posVsNegDist(data):

    x = np.random.randint(0,100,size=100,dtype=int)
    print(x,'\n')
    frequency,bins = np.histogram(x,bins=10,range=[0,20])

    for b,f in zip(bins[1:],frequency):
        print(round(b,2),''.join(np.repeat('*',f)))

    y = np.random.normal(size=1000)
    plt.hist(y, bins=50)
    plt.gca().set(title='Positive vs Negative Distribution', ylabel='Frequency',);

    plt.hist(data, bins=2, align='mid',color='blue',density='true',
             histtype='stepfilled',bottom=0,)  #normed='true',density='true',stacked='true'

    plt.show()

def numericalFeatureDist(data):

    categoriesDict = {0: "Age", 2:'fnlwgt',4:'Education-Num',10:'Capital-Gain',
                      11:'Capital-loss',12:'Hours-per-week'}
    h = [0,2,4,10,11,12]
    for i in h:
        plt.gca().set(title='Category Distribution', ylabel='Frequency', xlabel='Category : ' + str(categoriesDict[i]))
        plt.hist(data[:,i],bins=60,density='true')
        plt.show()

        break

def corrBetFeatures(data):
    df = pd.DataFrame(data)
    #print(df)
    #Correlation between age and capital gains
    #df[data[:,0]].corr(data[:,10])
    correlationMatrix = df.corr()
    print(correlationMatrix)

def pairwise(data):
    data = np.log10(data+1)          #Converting data to logs and +1 for the zero values
    print('\n',data)



    # df = pd.DataFrame(data)
    # #print(dfLogs)
    # snsData = sns.load_dataset(data)
    # #df.rename(columns={0:"Age",1:"aSD"})
    # #print(df)
    # sns.set(style="darkgrid")
    # #sns.pairplot(df)
    # sns.relplot(x="time", y="value", kind="scatter", data=df)
    # print("~~ sns pairplot complete ~~ ")



    plt.show()


if __name__ == '__main__':
    adult = dataAnalyze.AdultAnalyze()
    dataMatrix = adult.analyzeData()
    yVector = adult.getY()

    posVsNegDist(yVector)                  #### UNCOMMENT TO TEST
    #numericalFeatureDist(dataMatrix)       #### UNCOMMENT TO TEST
    #corrBetFeatures(dataMatrix)             #### UNCOMMENT TO TEST
    #pairwise(dataMatrix)                    ##### UNCOMMENT TO TEST


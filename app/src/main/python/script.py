from pytrends.request import TrendReq

def getResult(word_01, word_02, word_03, word_04, word_05):

    pytrends = TrendReq(hl='en-US', tz=360)
    kwList = [word_01, word_02, word_03, word_04, word_05]
    keywords = []

    s = "Trend %" + "\n"

    def check():
        pytrends.build_payload(kwList, cat=0, timeframe='now 7-d', geo='', gprop='')
        data = pytrends.interest_over_time()
        mean = round(data.mean(), 2)
        return str(mean[kw]) + "\n"

    kwList = [x for x in kwList if x != ""]


    for kw in kwList:
        keywords.append(kw)
        s = s + kw + " " + check()
        keywords.pop()

    return s

from pytrends.request import TrendReq

def test(word_01, word_02, word_03, word_04, word_05):
    return word_01 + " " + word_02 + " " +  word_03 + " " + word_04 + " "+ word_05

def run():
    return get_stats()

#def get_stats():
def get_stats(word_01, word_02, word_03, word_04, word_05):

    pytrends = TrendReq(hl='en-US', tz=360)
    #kw_list = ["Ted Lasso Season 2", "nike travis scott", "monkeypox", "ukraine"]
    kw_list = [word_01, word_02, word_03, word_04, word_05]
    keywords = []

    s = "Trend %" + "\n"

    def check():
        pytrends.build_payload(kw_list, cat=0, timeframe='now 7-d', geo='', gprop='')
        data = pytrends.interest_over_time()
        mean = round(data.mean(), 2)
        #print(kw + ': ' + str(mean[kw]))
        #s = s + kw + ': ' + str(mean[kw]) + '\n'i
        return str(mean[kw]) + "\n"

    kw_list = [x for x in kw_list if x != ""]


    for kw in kw_list:
        keywords.append(kw)
        s = s + kw + " " + check()
        keywords.pop()

    return s

# print(run())

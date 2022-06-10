from pytrends.request import TrendReq

def run():
    return get_stats()

def get_stats():
    pytrends = TrendReq(hl='en-US', tz=360)
    kw_list = ["Ted Lasso Season 2", "nike travis scott", "monkeypox", "ukraine"]
    keywords = []

    s = "Trend %" + "\n"

    def check():
        pytrends.build_payload(kw_list, cat=0, timeframe='now 7-d', geo='', gprop='')
        data = pytrends.interest_over_time()
        mean = round(data.mean(), 2)
        #print(kw + ': ' + str(mean[kw]))
        #s = s + kw + ': ' + str(mean[kw]) + '\n'i
        return str(mean[kw]) + "\n"


    for kw in kw_list:
        keywords.append(kw)
        s = s + kw + " " + check()
        keywords.pop()

    return s

# print(run())

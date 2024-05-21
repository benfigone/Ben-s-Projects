import pygame
def level_map(num,win):
    if num == 1:
        pygame.draw.rect(win, (255,0,0), (215,0,10,1000))
        pygame.draw.rect(win, (255,0,0), (325,0,10,1000))
        pygame.draw.rect(win, (0,255,0), (225,0,100,20))
        green = pygame.color.Color((255, 255, 255))
        font = pygame.font.SysFont('freesansbold.ttf', 13)
        text = font.render("Hello and Welcome to Obstical Avoider!", True, green)
        win.blit(text, (0,225))
        pygame.display.flip()
        text = font.render("Goal: Reach the green finish line while avoiding surfaces", True, green)
        win.blit(text, (0,260))
        pygame.display.flip()
        text = font.render("To move, use the arrow keys", True, green)
        win.blit(text, (0,295))
        pygame.display.flip()
    if num ==2:
        pygame.draw.rect(win, (255,0,0), (245,0,10,290))
        pygame.draw.rect(win, (255,0,0), (295,0,10,240))
        pygame.draw.rect(win, (255,0,0), (295,240,240,10))
        pygame.draw.rect(win, (255,0,0), (245,290,220,10))
        pygame.draw.rect(win, (255,0,0), (465,290,10,180))
        pygame.draw.rect(win, (255,0,0), (265,460,210,10))
        pygame.draw.rect(win, (255,0,0), (255,460,10,60))
        pygame.draw.rect(win, (0,255,0), (255,0,40,10))


    if num == 3:
        pygame.draw.rect(win, (255,0,0), (245,0,10,100))
        pygame.draw.rect(win, (255,0,0), (295,0,10,150))
        pygame.draw.rect(win, (255,0,0), (195,150,170,10))
        pygame.draw.rect(win, (255,0,0), (195,70,10,80))
        pygame.draw.rect(win, (255,0,0), (145,30,100,10))
        pygame.draw.rect(win, (255,0,0), (145,30,10,170))
        pygame.draw.rect(win, (255,0,0), (145,200,160,10))
        pygame.draw.rect(win, (255,0,0), (365,150,10,100))
        pygame.draw.rect(win, (255,0,0), (205,250,170,10))
        pygame.draw.rect(win, (255,0,0), (145,200,10,250))
        pygame.draw.rect(win, (255,0,0), (205,250,10,150))
        pygame.draw.rect(win, (255,0,0), (145,450,100,10))
        pygame.draw.rect(win, (255,0,0), (245,450,10,100))
        pygame.draw.rect(win, (255,0,0), (205,400,100,10))
        pygame.draw.rect(win, (255,0,0), (305,400,10,100))
        pygame.draw.rect(win, (0,255,0), (255,0,40,10))

    if num == 4:
        red = pygame.color.Color((255, 0, 0))
        font = pygame.font.SysFont('freesansbold.ttf', 32)
        text = font.render("You Won!", True, red)

        win.blit(text, (200,225))
        pygame.display.flip()

    if num == 5:
        red = pygame.color.Color((255, 0, 0))
        font = pygame.font.SysFont('freesansbold.ttf', 32)
        text = font.render("(-_-) - Gob ears", True, red)

        win.blit(text, (200,225))
        pygame.display.flip()


def update_obs(num,z,win):
        pygame.draw.rect(win, (255,100,100), (z,150,20,20))
        pygame.display.update()

def update_obsW(num,w,win):
        pygame.draw.rect(win, (255,100,100), (w,170,20,20))
        pygame.display.update()

def update_obsK(num,k,win):
        pygame.draw.rect(win, (255,100,100), (250,k,20,20))
        pygame.display.update()

def update_obsVR(num,v,r,win):
        pygame.draw.rect(win, (255,100,100), (v,r,20,20))
        pygame.display.update()

def touching_obstical(num, x1, y1):
    if num == 1:
        if x1 < 225 or x1 > 305:
            return True
        else:
            return False
    if num == 2:
        if x1 < 250:
            return True
        if x1 > 275 and y1 < 250:
            return True
        if 465 > x1 > 250 and 270 <y1 < 280:
            return True
        if  475 > x1  and 270 < y1 < 470:
            return True
        if  265 > x1  and 460 < y1 < 500:
            return True
    if num == 3:
        if x1 < 250 and y1 > 430:
            return True
        if x1 > 285 and y1 > 400:
            return True
        if 185 < x1 < 500 and 230 < y1 < 410:
            return True
        if x1 < 155 and 150 < y1 < 500:
            return True
        if x1 > 345 and 0 < y1 < 300:
            return True
        if 0 < x1 < 305 and 180 < y1 < 210:
            return True
        if 175 < x1 < 500 and 125 < y1 < 160:
            return True
        if 175 < x1 < 195 and 50 < y1 < 160:
            return True
        if 145 < x1 < 245 and 25 < y1 < 40:
            return True
        if 225 < x1 < 250 and 25 < y1 < 100:
            return True
        if 275 < x1 < 300 and 0 < y1 < 150:
            return True
        else:
            return False




def hit_box_rect(z1,x1,y1):
    x2 = x1 + 20
    y2 = y1 + 20
    z2 = z1 + 20
    if y1 in range(150,171) and x1 in range(z1,z2+1):
        return True
    if y2 in range(150,171) and x2 in range(z1,z2+1):
        return True
    return False

def hit_box_rectW(w1,x1,y1):
    x2 = x1 + 20
    y2 = y1 + 20
    w2 = w1 + 20
    if y1 in range(171,190) and x1 in range(w1,w2+1):
        return True
    if y2 in range(171,190) and x2 in range(w1,w2+1):
        return True
    return False

def hit_box_rectK(k1,x1,y1):
    x2 = x1 + 20
    y2 = y1 + 20
    k2 = k1 + 20
    if x1 in range(251,270) and y1 in range(k1,k2+1):
        return True
    if x2 in range(251,270) and y2 in range(k1,k2+1):
        return True
    return False

def hit_box_rectVR(v1,r1,x1,y1):
    x2 = x1 + 20
    y2 = y1 + 20
    v2 = v1 + 20
    r2 = r1 + 20
    if x1 in range(r1,r2+1) and y1 in range(v1,v2+1):
        return True
    if x2 in range(r1,r2+1) and y2 in range(v1,v2+1):
        return True
    return False

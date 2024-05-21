import pygame
from Helper import *
pygame.init()

win = pygame.display.set_mode((500,500))

pygame.display.set_caption("Obstical Avoider")
v = 250
v_vel = 5
r = 250
r_vel = 5
k = 250
k_vel = 5
w = 250
w_vel = 5
z_vel = 5
z = 250
x = 268
y = 480
width = 20
height = 20
spd = 5
num = 1
run = True

while run:
    pygame.time.delay(100)

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            run = False



    postion = [x,y]

    key = pygame.key.get_pressed()
    if key[pygame.K_DOWN] and y < 500 - height - spd:
        y = y + spd
    if key[pygame.K_LEFT] and x > spd:
        x = x - spd
    if key[pygame.K_RIGHT] and x < 500 - width - spd:
        x = x + spd
    if key[pygame.K_UP] and y > spd:
        y = y - spd


    if postion[1] < 20:
        x = 268
        y = 480
        num += 1


    if touching_obstical(num,x,y) == True:
        x = 268
        y = 480

    win.fill((0,0,0))

    if num == 2:
        if hit_box_rect(z,x,y) == True:
            x = 276
            y= 480

    if num == 3:
        if hit_box_rectW(w,x,y) == True:
            x = 276
            y= 480
        if hit_box_rectK(k,x,y) == True:
            x = 276
            y= 480
        if hit_box_rectVR(v,r,x,y) == True:
            x = 276
            y = 480


    if num == 2:
        z += z_vel
        update_obs(num,z,win)
        if z < 200:
            z_vel = 5
        if z > 340:
            z_vel = -5

    if num == 3:
        w += w_vel
        update_obsW(num,w,win)
        if w < 5:
            w_vel = 5
        if w > 500:
            w_vel = -5
        k += k_vel
        update_obsK(num,k,win)
        if k < 100:
            k_vel = 5
        if k > 300:
            k_vel = -5
        r += r_vel
        v += v_vel
        update_obsVR(num,v,r,win)
        if r < 100:
            r_vel = 5
            v_vel = 5
        if r > 400:
            r_vel = -5
            v_vel = -5

    level_map(num,win)
    pygame.draw.rect(win, (255, 255, 255), (x, y, width, height))
    pygame.display.update()




pygame.quit()

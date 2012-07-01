import sys
import random

class Mat:
	x=[ ]

	h=1
	w=1

	def __init__(self,h,w,c):
		self.x=[]
		self.h=h
		self.w=w

		for i in range(h):
			self.x.append([c]*w)

	def mprint(self):
		print '  012345678901234567890123456789'
		print ' +------------------------------'
		for i in range(0,self.h):
			sys.stdout.write(chr(ord('0')+(i%10)))
			sys.stdout.write('|')
			for j in range(0,self.w):
				s=self.mget(i,j)
				sys.stdout.write(s)
			sys.stdout.write('\n')

	def mget(self,i,j):
		return self.x[i][j]

	def mset(self,i,j,a):
		self.x[i][j]=a


class MineSweeper:

	h=1
	w=1
	b=1

	user=Mat(h,w,'X')	
	admin=Mat(h,w,'0')

	def __init__(self,h,w,b):
		self.h=h
		self.w=w
		self.b=b

		self.user=Mat(h,w,'X')	
		self.admin=Mat(h,w,'0')

	def setBombs(self):
		for y in range(0,self.b):
			while 0==0:
				n=random.randint(0,self.h*self.w-1)
				i=n/self.w
				j=n%self.w
				if self.admin.mget(i,j) =='9':
					continue
				else:
					break
			self.admin.mset(i,j,'9')

	def check(self,i,j):
		if self.admin.mget(i,j)=='9':
			return 9
		q=0
		if i>=1 and j>=1:
			if self.admin.mget(i-1,j-1)=='9':
				q=q+1
		if j>=1:
			if self.admin.mget(i,j-1)=='9':
				q=q+1
		if i<=self.h-2 and j>=1:
			if self.admin.mget(i+1,j-1)=='9':
				q=q+1
		if i>=1:
			if self.admin.mget(i-1,j)=='9':
				q=q+1
		if i<=self.h-2:
			if self.admin.mget(i+1,j)=='9':
				q=q+1
		if i>=1 and j<=self.w-2:
			if self.admin.mget(i-1,j+1)=='9':
				q=q+1
		if j<=self.w-2:
			if self.admin.mget(i,j+1)=='9':
				q=q+1
		if i<=self.h-2 and j<=self.w-2:
			if self.admin.mget(i+1,j+1)=='9':
				q=q+1
		return self.admin.mset(i,j,chr(ord('0')+q))

	def checkAll(self):
		for i in range(0,self.h):
			for j in range(0,self.w):
				self.check(i,j)

	def turn(self,i,j,k):
		if k==1:
			if self.user.mget(i,j)!='X':
				print "you don't have to frag here"
				return 0
			self.user.mset(i,j,'F')	
			self.user.mprint()
			return 0

		d=self.admin.mget(i,j)
		if d=='9':
			print 'game over'
			self.admin.mprint()
			return -1
		else:
			if self.user.mget(i,j)=='F':
				self.user.mset(i,j,'X')
			self.open(i,j)
			l=self.win()
			self.user.mprint()
			return l


	def win(self):
		w=0
		for i in range(0,self.h):
			for j in range(0,self.w):
				if self.user.mget(i,j)!='F' and self.user.mget(i,j)!='X':
					w=w+1
					if w==self.h*self.w-self.b:
						print 'congratulations!'
						return 1
		return 0

	def open(self,i,j):
		d=self.admin.mget(i,j)
		if d=='9':
			return

		if self.user.mget(i,j)!='X':
			return

		self.user.mset(i,j,d)
		if d!='0':
			return
		if i>=1 and j>=1:
			self.open(i-1,j-1)
		if j>=1:
			self.open(i,j-1)
		if i<=self.h-2 and j>=1:
			self.open(i+1,j-1)				
		if i>=1:
			self.open(i-1,j)
		if i<=self.h-2:
			self.open(i+1,j)		
		if i>=1 and j<=self.w-2:
			self.open(i-1,j+1)
		if j<=self.w-2:
			self.open(i,j+1)
		if i<=self.h-2 and j<=self.w-2:
			self.open(i+1,j+1)

ms=MineSweeper(16,30,99)
ms.setBombs()
ms.checkAll()	
#ms.admin.mprint()
print
ms.user.mprint()

while 0==0:
	i=input("please enter i=")
	if i>=ms.h:
		print "enter the number "
		continue
	j=input("please enter j=")
	if j>=ms.w:
		print "enter the number "
		continue
	k=input("please enter F=")
	if k!=0 and k!=1:
		print "enter 0(open) or 1(frag)"
		continue
	u=ms.turn(i,j,k)
	if u!=0:
		break

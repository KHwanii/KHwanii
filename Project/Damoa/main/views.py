from .models import Club, Board, Reply
from django.shortcuts import render, get_object_or_404, redirect
from django.core.paginator import Paginator
from django.contrib.auth.decorators import login_required
from django.utils import timezone
from .forms import BoardForm, ClubForm, ReplyForm
from django.contrib import messages
from django.db.models import Q, Count

def index(request):
    """
    첫 화면 출력
    """
    return render(request, 'main/index.html')


def main(request):
    """
    메인 화면 출력
    """
    club_list=Club.objects.order_by('-id')

    page = request.GET.get('page', '1')  # 페이지
    paginator = Paginator(club_list, 6)  # 페이지당 6개
    page_obj = paginator.get_page(page)

    context={'club_list':page_obj, 'page':page}
    return render(request, 'main/main.html', context)


def detail(request, board_id):
    '''
    board내용 출력
    '''
    board=get_object_or_404(Board, pk=board_id)
    context={'board':board}
    return render(request, 'main/board_detail.html',context)


def login(request):
    """
    로그인 화면 출력
    """
    return render(request, 'templates/common/login.html')   


def club_create(request):
    '''
    클럽등록
    '''
    if request.method=='POST':
        form=ClubForm(request.POST)
        if form.is_valid():
            club=form.save(commit=False)
            club.author = request.user
            club.save()
            return redirect('main:main')
    else:
        form=ClubForm()
    return render(request, 'main/club_form.html', {'form':form})


@login_required(login_url = 'common:login')
def club_delete(request, club_id):
    """
    클럽 삭제
    """
    club = get_object_or_404(Club, pk = club_id)
    if request.user != club.author:
        messages.error(request, '삭제 권한이 없습니다.')
        return redirect('main:main', club_id = club.id)
    club.delete()
    return redirect('main:main')
    

def board_list(request):
    '''
    board 출력
    '''
    
    # 입력 인자
    page = request.GET.get('page', '1')  # 페이지
    kw = request.GET.get('kw', '')  # 검색어
    so = request.GET.get('so', 'recent') # 정렬 기준 / default 최신순

    if so == 'recent':
        board_list = Board.objects.order_by('-create_date')
    elif so == 'late':
        board_list = Board.objects.order_by('create_date')
    elif so == 'recommend':
        board_list = Board.objects.annotate(
            num_voter = Count('voter')).order_by('-num_voter', '-create_date')
    elif so == 'popular':
        board_list = Board.objects.annotate(
            num_reply = Count('reply')).order_by('-num_reply', '-create_date')
    else : # 위 경우 제외 board_id 역순정렬
        board_list = Board.objects.order_by('-id')

    if kw:
        kw = kw.replace('년','')
        kw = kw.replace('월','')
        kw = kw.replace('일','')
        board_list = board_list.filter(
            Q(subject__icontains=kw) |  # 제목 검색
            Q(content__icontains=kw) |  # 내용 검색
            Q(author__name__icontains=kw) |  # 작성자 검색
            Q(club__name__icontains=kw) |    # 클럽 이름 검색
            Q(club__category__icontains=kw) |  # 클럽 카테고리 검색
            Q(event_date__icontains=kw)      # 모임일 검색
        ).distinct()

    paginator = Paginator(board_list, 10)  # 페이지당 10개 
    page_obj = paginator.get_page(page)
    context = {'board_list':page_obj, 'page':page, 'kw':kw, 'so':so}

    return render(request, 'main/board_list.html', context)



@login_required(login_url = 'common:login')
def board_create(request):
    '''
    게시글 등록
    '''

    if request.method=='POST':
        form=BoardForm(request.POST, user=request.user)
        if form.is_valid():
            board=form.save(commit=False)
            board.author=request.user
            board.create_date = timezone.now()
            board.save()
            return redirect('main:board_list')
    else:
        form=BoardForm(user=request.user)
            
    return render(request, 'main/board_form.html', {'form':form})


@login_required(login_url = 'common:login')
def board_modify(request, board_id):
    """
    게시글 수정
    """
    board = get_object_or_404(Board, pk = board_id)
    if request.user != board.author:
        messages.error(request, '수정 권한이 없습니다.')
        return redirect('main:detail', board_id = board.id)
    
    if request.method == 'POST':
        form = BoardForm(request.POST, instance = board, user=request.user)
        if form.is_valid():
            board = form.save(commit = False)
            board.author = request.user
            board.modify_date = timezone.now()
            board.save()
            return redirect('main:detail', board_id = board.id)
    else:
        form = BoardForm(instance=board, user=request.user)  # 기존 내용이 반영된 상태에서 수정 시작
    context = {'form': form}
    return render(request, 'main/board_form.html', context)



@login_required(login_url = 'common:login')
def board_delete(request, board_id):
    """
    게시글 삭제
    """
    board = get_object_or_404(Board, pk = board_id)
    if request.user != board.author:
        messages.error(request, '삭제 권한이 없습니다.')
        return redirect('main:detail', board_id = board.id)
    board.delete()
    return redirect('main:board_list')


@login_required(login_url = 'common:login')
def reply_create(request, board_id):
    '''
    댓글 등록
    '''
    board=get_object_or_404(Board, pk=board_id)
    if request.method=='POST':
        form=ReplyForm(request.POST)
        if form.is_valid():
            reply=form.save(commit=False)
            reply.author=request.user
            reply.create_date=timezone.now()
            reply.board=board
            reply.save()
            return redirect('main:detail', board_id=board.id)
    else:
        form=ReplyForm()
    context={'board':board, 'form':form}            
    return render(request, 'main/board_detail.html', context)


@login_required(login_url = 'common:login')
def reply_modify(request, reply_id):
    """
    답변 수정
    """
    reply = get_object_or_404(Reply, pk = reply_id)
    if request.user != reply.author:
        messages.error(request, '수정 권한이 없습니다.')
        return redirect('main:detail', board_id = reply.board.id)
    
    if request.method == "POST":
        form = ReplyForm(request.POST, instance = reply)
        if form.is_valid():
            reply = form.save(commit = False)
            reply.author = request.user
            reply.modify_date = timezone.now()
            reply.save()
            return redirect('main:detail', board_id = reply.board.id)
    else:
        form = ReplyForm(instance = reply)
    context = {'reply': reply, 'form':form }
    return render(request, 'main/reply_form.html', context)


@login_required(login_url='common:login')
def reply_delete(request, reply_id):
    """
    답변 삭제
    """
    reply = get_object_or_404(Reply, pk = reply_id)
    if request.user != reply.author:
        messages.error(request, '삭제 권한이 없습니다.')
    else:
        reply.delete()
    return redirect('main:detail', board_id = reply.board.id)


@login_required(login_url='common:login')
def vote_board(request, board_id):
    '''
    참석
    '''
    board = get_object_or_404(Board, pk=board_id)
    if request.user == board.author:
        messages.error(request, '본인이 작성한 글은 참석을 누를 수 없습니다.')
    elif request.user in board.voter.all():
        board.voter.remove(request.user)
    else:
        board.voter.add(request.user)
    return redirect('main:detail', board_id=board.id)


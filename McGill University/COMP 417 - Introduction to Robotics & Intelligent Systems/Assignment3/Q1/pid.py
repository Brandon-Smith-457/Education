from time import time

class PIDController:
    def __init__(self, target_pos):
        self.target_pos = target_pos
        self.Kp = 7000.00
        self.Ki = 300.00
        self.Kd = 2000.00
        self.bias = 0.0
        self.lastTargetPos = target_pos
        self.errorHistPos = 0
        self.errorHistSize = 10000
        # Unfortunately due to computational restrictions our integral term is constricted to a "history" of 10'000 time steps,
        # however this means that if we hover in the same spot for too long then we'll have an integral of 0 which will cause
        # the system to have to "re-callibrate".
        self.errorHist = [0] * self.errorHistSize
        self.lastTime = time()
        self.dt = time() - self.lastTime
        self.maxIntegralError = 100
        return

    def reset(self):
        self.errorHistPos = 0
        self.errorHist = [0] * self.errorHistSize
        return

#TODO: Complete your PID control within this function. At the moment, it holds
#      only the bias. Your final solution must use the error between the
#      target_pos and the ball position, plus the PID gains. You cannot
#      use the bias in your final answer.
    def get_fan_rpm(self, vertical_ball_position):
        currentTime = time()
        self.dt = currentTime - self.lastTime
        self.lastTime = currentTime

        pError = self.target_pos - vertical_ball_position
        self.errorHist[self.errorHistPos] = pError
        pTerm = self.Kp * pError

        # Dealing with the problem of comparing different target positions in the derivative term causing the ball to shoot up
        if self.target_pos != self.lastTargetPos:
            dError = 0
        else:
            dError = (self.errorHist[self.errorHistPos] - self.errorHist[(self.errorHistPos - 1) % self.errorHistSize]) / self.dt
        dTerm = self.Kd * dError

        iError = 0
        for error in self.errorHist:
            iError += error
        if iError >= 0:
            iError = min([iError, self.maxIntegralError])
        elif iError < 0:
            iError = max([iError, -self.maxIntegralError])
        iTerm = self.Ki * iError

        self.errorHistPos = (self.errorHistPos + 1) % self.errorHistSize
        self.lastTargetPos = self.target_pos

        output = pTerm + dTerm + iTerm
        return output
